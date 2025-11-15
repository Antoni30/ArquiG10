// src/pages/RealizarRetiro.jsx
import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

// 🚀 Código de empleado quemado (hardcodeado)
const EMPLEADO_FIJO = '0002'; 

const RealizarRetiro = () => {
  const [cuenta, setCuenta] = useState('');
  const [importe, setImporte] = useState('');
  // ❌ Eliminado: Se quita el estado para el empleado
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
    
    // 💡 Usamos el código de empleado quemado
    const empleadoCodigo = EMPLEADO_FIJO; 

    try {
      // 1. Validar cuenta
      if (!cuenta.trim()) throw new Error('El número de cuenta es obligatorio.');
      let cuentaExiste = false;
      try {
        const res = await api.get(`/cuentas/${cuenta}`);
        cuentaExiste = res.data != null;
      } catch {
        cuentaExiste = false;
      }
      if (!cuentaExiste) throw new Error(`La cuenta "${cuenta}" no existe.`);

      // 2. Validar importe
      const monto = parseFloat(importe);
      if (isNaN(monto) || monto <= 0) throw new Error('El importe debe ser mayor a 0.');

      // 3. 🚀 Validar Empleado (Usamos el código quemado, pero validamos que exista en la API)
      let empleadoExiste = false;
      try {
        const res = await api.get(`/empleados/${empleadoCodigo}`);
        empleadoExiste = res.data != null;
      } catch {
        empleadoExiste = false;
      }
      if (!empleadoExiste) throw new Error(`El empleado con código fijo "${empleadoCodigo}" no existe.`);

      // 4. Enviar
      await api.post('/cuentas/retiro', {
        cuentaCodigo: cuenta,
        importe: monto,
        // Usamos el código de empleado quemado
        empleadoCodigo: empleadoCodigo
      });

      setSuccess('✅ Retiro procesado con éxito.');
      setCuenta('');
      setImporte('');
      // ❌ Eliminado: No es necesario resetear el estado de empleado
      // setEmpleado('');

    } catch (err) {
      const errorMessage = err.message || 'Error desconocido al procesar el retiro.';
      setError(`❌ ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout imageUrl="/images/retiro-bg.png" imagePosition="left">
      <div className="form-card card-retiro">
        <h2>💸 Realizar Retiro</h2>
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
          </div> */}


          <button
            type="submit"
            className="btn btn-danger"
            disabled={loading}
          >
            {loading ? 'Procesando...' : 'Confirmar Retiro'}
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

export default RealizarRetiro;