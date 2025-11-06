import React, { useState } from 'react';
import { convertirUnidad } from '../services/restClient';

const CONVERSION_CATEGORIES = {
    'Longitud': {
        icon: '📏',
        data: [
            { label: 'Centímetros a Metros', method: 'centimetros-a-metros', unitIn: 'cm', unitOut: 'm' },
            { label: 'Metros a Centímetros', method: 'metros-a-centimetros', unitIn: 'm', unitOut: 'cm' },
            { label: 'Metros a Kilómetros', method: 'metros-a-kilometros', unitIn: 'm', unitOut: 'km' },
        ],
    },
    'Masa': {
        icon: '⚖️',
        data: [
            { label: 'Tonelada a Libra', method: 'tonelada-a-libra', unitIn: 'ton', unitOut: 'lb' },
            { label: 'Kilogramo a Libra', method: 'kilogramo-a-libra', unitIn: 'kg', unitOut: 'lb' },
            { label: 'Gramo a Kilogramo', method: 'gramo-a-kilogramo', unitIn: 'g', unitOut: 'kg' },
        ],
    },
    'Temperatura': {
        icon: '🌡️',
        data: [
            { label: 'Celsius a Fahrenheit', method: 'celsius-a-fahrenheit', unitIn: '°C', unitOut: '°F' },
            { label: 'Fahrenheit a Celsius', method: 'fahrenheit-a-celsius', unitIn: '°F', unitOut: '°C' },
            { label: 'Celsius a Kelvin', method: 'celsius-a-kelvin', unitIn: '°C', unitOut: 'K' },
        ],
    },
};

const Conversiones = ({ onLogout }) => {
    const [valor, setValor] = useState('');
    const [operacion, setOperacion] = useState('');
    const [resultado, setResultado] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [activeTab, setActiveTab] = useState('Longitud');

    const findSelectedOperation = (opMethod) => {
        for (const key in CONVERSION_CATEGORIES) {
            const op = CONVERSION_CATEGORIES[key].data.find(c => c.method === opMethod);
            if (op) return op;
        }
        return null;
    };

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
            const res = await convertirUnidad(inputVal, operacion);

            // Si el resultado es -1, es un error del servidor (según tu lógica anterior)
            if (res.resultado === -1) {
                setError(res.mensaje || 'Error de validación del servidor.');
                setResultado(null);
            } else {
                const formattedInput = inputVal.toFixed(2);
                const formattedResult = res.resultado.toFixed(2);
                setResultado(`${formattedInput} ${convInfo.unitIn} = ${formattedResult} ${convInfo.unitOut}`);
            }
        } catch (err) {
            setError(err.message || 'Ocurrió un error al consumir el servicio REST.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container conversion-container">
            <button onClick={onLogout} className="logout-button">Cerrar Sesión</button>
            <h1>Web Service de Conversiones (RESTful)</h1>

            <div className="tab-navigation">
                {Object.keys(CONVERSION_CATEGORIES).map(tabName => (
                    <button
                        key={tabName}
                        className={`tab-button ${activeTab === tabName ? 'active' : ''}`}
                        onClick={() => {
                            setActiveTab(tabName);
                            setOperacion('');
                            setValor('');
                            setResultado(null);
                            setError('');
                        }}
                    >
                        {CONVERSION_CATEGORIES[tabName].icon} {tabName}
                    </button>
                ))}
            </div>

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

            {resultado && <p className="message result-success">{resultado}</p>}
            {error && <p className="message result-error">{error}</p>}
        </div>
    );
};

export default Conversiones;