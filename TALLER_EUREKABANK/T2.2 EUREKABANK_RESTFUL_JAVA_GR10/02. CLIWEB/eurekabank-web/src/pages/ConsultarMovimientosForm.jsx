// src/pages/ConsultarMovimientosForm.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

const ConsultarMovimientosForm = () => {
  const [cuenta, setCuenta] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!cuenta.trim()) {
      setError('❌ El número de cuenta es obligatorio.');
      return;
    }
    // Validar que sea solo números
    if (!/^\d+$/.test(cuenta)) {
      setError('❌ El número de cuenta debe contener solo dígitos.');
      return;
    }
    navigate(`/movimientos/${cuenta}`);
  };

  return (
    <Layout imageUrl="/images/movimientos-bg.png" imagePosition="left">
      <div className="form-card card-movimientos">
        <h2>📈 Consultar Movimientos</h2>
        {error && <div className="alert alert-error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Número de cuenta</label>
            <input
              type="text"
              placeholder="Número de cuenta"
              value={cuenta}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, '');
              }}
              onChange={(e) => setCuenta(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Consultar
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

export default ConsultarMovimientosForm;