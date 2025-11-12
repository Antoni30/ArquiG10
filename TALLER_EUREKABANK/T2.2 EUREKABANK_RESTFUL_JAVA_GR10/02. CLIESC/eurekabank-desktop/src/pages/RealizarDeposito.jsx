// src/pages/RealizarDeposito.jsx
import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

// 🚀 Empleado Fijo: Definimos el código quemado
// Usaremos un código fijo como '001', o uno aleatorio entre '001' y '002' si prefieres esa lógica.
// Para este ejemplo, usaremos '001'. Puedes cambiarlo a '002' si quieres uno fijo.
const EMPLEADO_FIJO = '0001'; 
// Si quieres que alterne entre '001' y '002':
// const EMPLEADO_FIJO = Math.random() < 0.5 ? '001' : '002'; 

const RealizarDeposito = () => {
  const [cuenta, setCuenta] = useState('');
  const [importe, setImporte] = useState('');
  // ❌ Eliminado: Eliminamos el estado para el empleado
  // const [empleado, setEmpleado] = useState(''); 
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    // Asignamos el código de empleado quemado
    const empleadoCodigo = EMPLEADO_FIJO; 

    try {
      // 1. Validar cuenta
      if (!cuenta.trim()) throw new Error('El número de cuenta es obligatorio.');
      let cuentaExiste = false;
      try {
        const resCuenta = await api.get(`/cuentas/${cuenta}`);
        cuentaExiste = resCuenta.data != null;
      } catch {
        cuentaExiste = false;
      }
      if (!cuentaExiste) throw new Error(`La cuenta "${cuenta}" no existe.`);

      // 2. Validar importe
      const monto = parseFloat(importe);
      if (isNaN(monto) || monto <= 0) throw new Error('El importe debe ser un número mayor a 0.');

      // 3. 🚀 Validar Empleado (Usamos el código quemado, pero validamos que exista en la API)
      let empleadoExiste = false;
      try {
        const resEmp = await api.get(`/empleados/${empleadoCodigo}`);
        empleadoExiste = resEmp.data != null;
      } catch {
        empleadoExiste = false;
      }
      if (!empleadoExiste) throw new Error(`El empleado con código fijo "${empleadoCodigo}" no existe.`);

      // 4. Enviar
      await api.post('/cuentas/deposito', {
        cuentaCodigo: cuenta,
        importe: monto,
        // Usamos el código de empleado quemado
        empleadoCodigo: empleadoCodigo 
      });

      setSuccess('✅ Depósito procesado con éxito.');
      setCuenta('');
      setImporte('');
      // ❌ Eliminado: No es necesario resetear el estado de empleado
      // setEmpleado(''); 

    } catch (err) {
      // Si el error es una instancia de Error, usamos su mensaje, sino un mensaje genérico
      const errorMessage = err.message || 'Error desconocido al procesar el depósito.';
      setError(`❌ ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout imageUrl="/images/deposito-bg.png" imagePosition="right">
      <div className="form-card card-deposito">
        <h2>💰 Realizar Depósito</h2>
        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Número de cuenta</label>
            <input
              type="text"
              placeholder="Número de cuenta"
              value={cuenta}
              onChange={(e) => setCuenta(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div>
          <div className="form-group">
            <label>Cantidad</label>
            <input
              type="number"
              step="0.01"
              placeholder="Cantidad"
              value={importe}
              onChange={(e) => setImporte(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div>
          
          {/* ❌ Eliminado: Se quitó el campo de Código del empleado de la interfaz */}
          {/* <div className="form-group">
            <label>Código del empleado</label>
            <input
              type="text"
              placeholder="Código del empleado"
              value={empleado}
              onChange={(e) => setEmpleado(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div> 
          */}



          <button
            type="submit"
            className="btn btn-success"
            disabled={loading}
          >
            {loading ? 'Procesando...' : 'Confirmar Depósito'}
          </button>
        </form>

        <button
          onClick={() => navigate('/menu')}
          className="btn btn-primary"
          style={{ marginTop: '1.5rem' }}
        >
          ⬅️ Atrás al Menú
        </button>
      </div>
    </Layout>
  );
};

export default RealizarDeposito;