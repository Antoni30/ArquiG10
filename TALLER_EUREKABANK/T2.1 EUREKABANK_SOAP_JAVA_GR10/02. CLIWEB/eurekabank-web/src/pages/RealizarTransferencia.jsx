// src/pages/RealizarTransferencia.jsx
import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

const RealizarTransferencia = () => {
  const [cuentaOrigen, setCuentaOrigen] = useState('');
  const [cuentaDestino, setCuentaDestino] = useState('');
  const [importe, setImporte] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const validarCuenta = async (codigo) => {
    try {
      const res = await api.get(`/cuentas/${codigo}`);
      return res.data != null;
    } catch {
      return false;
    }
  };

  const validarEmpleado = async (codigo) => {
    try {
      const res = await api.get(`/empleados/${codigo}`);
      return res.data != null;
    } catch {
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Validar cuenta origen
      if (!cuentaOrigen.trim()) throw new Error('La cuenta origen es obligatoria.');
      const origenExiste = await validarCuenta(cuentaOrigen);
      if (!origenExiste) throw new Error(`La cuenta origen "${cuentaOrigen}" no existe.`);

      // Validar cuenta destino
      if (!cuentaDestino.trim()) throw new Error('La cuenta destino es obligatoria.');
      const destinoExiste = await validarCuenta(cuentaDestino);
      if (!destinoExiste) throw new Error(`La cuenta destino "${cuentaDestino}" no existe.`);

      // Validar que no sean iguales
      if (cuentaOrigen === cuentaDestino)
        throw new Error('Las cuentas origen y destino no pueden ser iguales.');

      // Validar importe
      const monto = parseFloat(importe);
      if (isNaN(monto) || monto <= 0)
        throw new Error('El importe debe ser mayor a 0.');

      // Código de empleado fijo (quemado)
      const empleadoCodigo = '0001';

      // Validar que el empleado fijo exista (opcional)
      const empExiste = await validarEmpleado(empleadoCodigo);
      if (!empExiste) throw new Error(`El empleado "${empleadoCodigo}" no existe.`);

      // Enviar transferencia
      await api.post('/cuentas/transferencia', {
        cuentaOrigen,
        cuentaDestino,
        importe: monto,
        empleadoCodigo,
      });

      setSuccess('✅ Transferencia procesada con éxito.');
      setCuentaOrigen('');
      setCuentaDestino('');
      setImporte('');

    } catch (err) {
      setError(`❌ ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout imageUrl="/images/transferencia-bg.png" imagePosition="right">
      <div className="form-card card-transferencia">
        <h2>🔄 Realizar Transferencia</h2>
        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Cuenta Origen</label>
            <input
              type="text"
              placeholder="Cuenta Origen"
              value={cuentaOrigen}
              onChange={(e) => setCuentaOrigen(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div>
          <div className="form-group">
            <label>Cuenta Destino</label>
            <input
              type="text"
              placeholder="Cuenta Destino"
              value={cuentaDestino}
              onChange={(e) => setCuentaDestino(e.target.value)}
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

          <button
            type="submit"
            className="btn btn-success"
            disabled={loading}
          >
            {loading ? 'Procesando...' : 'Confirmar Transferencia'}
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

export default RealizarTransferencia;
