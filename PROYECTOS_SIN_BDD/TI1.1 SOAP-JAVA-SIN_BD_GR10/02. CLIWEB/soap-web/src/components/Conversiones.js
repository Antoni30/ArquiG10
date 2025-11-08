import React, { useState } from 'react';
import callSoapService from '../soapClient'; 

// Nueva estructura de datos agrupada por categoría (sección/pestaña)
const CONVERSION_CATEGORIES = {
    'Longitud': {
        icon: '📏', // Icono para Longitud
        data: [
            { label: 'Centímetros a Metros', method: 'centimetrosAMetros', param: 'centimetros', unitIn: 'cm', unitOut: 'm' },
            { label: 'Metros a Centímetros', method: 'metrosACentimetros', param: 'metros', unitIn: 'm', unitOut: 'cm' },
            { label: 'Metros a Kilómetros', method: 'metrosAKilometros', param: 'metros', unitIn: 'm', unitOut: 'km' },
        ],
    },
    'Masa': {
        icon: '⚖️', // Icono para Masa
        data: [
            { label: 'Tonelada a Libra', method: 'toneladaALibra', param: 'toneladas', unitIn: 'ton', unitOut: 'lb' },
            { label: 'Kilogramo a Libra', method: 'kilogramoALibra', param: 'kilogramos', unitIn: 'kg', unitOut: 'lb' },
            { label: 'Gramo a Kilogramo', method: 'gramoAKilogramo', param: 'gramos', unitIn: 'g', unitOut: 'kg' },
        ],
    },
    'Temperatura': {
        icon: '🌡️', // Icono para Temperatura
        data: [
            { label: 'Celsius a Fahrenheit', method: 'celsiusAFahrenheit', param: 'celsius', unitIn: '°C', unitOut: '°F' },
            { label: 'Fahrenheit a Celsius', method: 'fahrenheitACelsius', param: 'fahrenheit', unitIn: '°F', unitOut: '°C' },
            { label: 'Celsius a Kelvin', method: 'celsiusAKelvin', param: 'celsius', unitIn: '°C', unitOut: 'K' },
        ],
    },
};

const Conversiones = ({ onLogout }) => {
    const [valor, setValor] = useState('');
    const [operacion, setOperacion] = useState('');
    const [resultado, setResultado] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    // Nuevo estado para la pestaña activa. Por defecto, 'Longitud'
    const [activeTab, setActiveTab] = useState('Longitud'); 

    // Función auxiliar para encontrar la operación seleccionada
    const findSelectedOperation = (opMethod) => {
        for (const key in CONVERSION_CATEGORIES) {
            const op = CONVERSION_CATEGORIES[key].data.find(c => c.method === opMethod);
            if (op) return op;
        }
        return null;
    };
    
    // Función de envío (ligeramente modificada para usar la nueva función de búsqueda)
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setResultado(null);

        const convInfo = findSelectedOperation(operacion);
        const inputVal = parseFloat(valor);

        if (!convInfo || isNaN(inputVal)) {
            setError('Seleccione una operación e ingrese un valor numérico válido.');
            return;
        }

        // Validación: solo Longitud y Masa no permiten valores negativos
        if ((activeTab === 'Longitud' || activeTab === 'Masa') && inputVal < 0) {
            setError('Error no permite negativos');
            return;
        }

        setLoading(true);
        try {
            const res = await callSoapService(convInfo.method, convInfo.param, inputVal);
            
            if (res === -1) {
                setError('Error de validación del servidor: El valor debe ser no negativo para esta unidad.');
                setResultado(null);
            } else {
                const formattedInput = inputVal.toFixed(2);
                const formattedResult = res.toFixed(2);

                setResultado(`${formattedInput} ${convInfo.unitIn} = ${formattedResult} ${convInfo.unitOut}`);
            }
        } catch (err) {
            setError(err.message || 'Ocurrió un error al consumir el servicio SOAP.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container conversion-container">
            <button onClick={onLogout} className="logout-button">Cerrar Sesión</button>
            <h1>Web Service de Conversiones (SOAP)</h1>
            
            {/* Pestañas de Navegación */}
            <div className="tab-navigation">
                {Object.keys(CONVERSION_CATEGORIES).map(tabName => (
                    <button
                        key={tabName}
                        className={`tab-button ${activeTab === tabName ? 'active' : ''}`}
                        onClick={() => {
                            setActiveTab(tabName);
                            setOperacion(''); // Limpiar la operación al cambiar de pestaña
                            setValor(''); // Limpiar el valor al cambiar de pestaña
                            setResultado(null);
                            setError('');
                        }}
                    >
                        {CONVERSION_CATEGORIES[tabName].icon} {tabName}
                    </button>
                ))}
            </div>

            {/* Formulario de Conversión */}
            <form onSubmit={handleSubmit}>
                <label>Valor a Convertir:</label>
                <input 
                    type="number" 
                    step="any" 
                    value={valor} 
                    onChange={(e) => setValor(e.target.value)} 
                    required 
                />
                
                <label>Seleccionar Conversión (Tipo: {activeTab}):</label>
                <select 
                    value={operacion} 
                    onChange={(e) => setOperacion(e.target.value)} 
                    required
                >
                    <option value="">-- SELECCIONE UNA OPERACIÓN --</option>
                    {CONVERSION_CATEGORIES[activeTab].data.map(c => (
                        <option key={c.method} value={c.method}>{c.label}</option>
                    ))}
                </select>

                <button type="submit" disabled={loading}>
                    {loading ? 'Procesando...' : 'Convertir'}
                </button>
            </form>

            {/* Mensajes de resultado y error con estilos CSS */}
            {resultado && <p className="message result-success">{resultado}</p>}
            {error && <p className="message result-error">{error}</p>}
        </div>
    );
};

export default Conversiones;