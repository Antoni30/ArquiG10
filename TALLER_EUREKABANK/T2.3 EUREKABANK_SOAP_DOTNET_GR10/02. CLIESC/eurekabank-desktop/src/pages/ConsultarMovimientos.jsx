// src/pages/ConsultarMovimientos.jsx
import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

const ConsultarMovimientos = () => {
  const [cuenta, setCuenta] = useState('');
  const [movimientos, setMovimientos] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Función para formatear fecha de YYYY-MM-DD a DD/MM/YYYY
  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const [year, month, day] = dateStr.split('-');
    return `${day}/${month}/${year}`;
  };

  // Función para obtener descripción y acción del tipo
  const getTipoInfo = async (tipoCodigo) => {
    try {
      const res = await api.get(`/tiposmovimiento/${tipoCodigo}`);
      if (res.data) {
        return {
          descripcion: res.data.descripcion || tipoCodigo,
          accion: res.data.accion || '?',
        };
      }
      return { descripcion: tipoCodigo, accion: '?' };
    } catch {
      return { descripcion: tipoCodigo, accion: '?' };
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (!cuenta.trim()) throw new Error('El número de cuenta es obligatorio.');

      // Validar existencia de la cuenta
      let cuentaExiste = false;
      try {
        const res = await api.get(`/cuentas/${cuenta}`);
        cuentaExiste = res.data != null;
      } catch {
        cuentaExiste = false;
      }
      if (!cuentaExiste) throw new Error(`La cuenta "${cuenta}" no existe.`);

      // Obtener movimientos
      const res = await api.get(`/cuentas/${cuenta}/movimientos`);
      let data = Array.isArray(res.data) ? res.data : [];

      // Ordenar por numero ascendente (1, 2, 3... 28)
      data.sort((a, b) => a.numero - b.numero);

      // Obtener información de tipo para cada movimiento
      const movimientosConInfo = await Promise.all(
        data.map(async (m) => {
          const tipoInfo = await getTipoInfo(m.tipoCodigo);
          return {
            ...m,
            tipoDescripcion: tipoInfo.descripcion,
            tipoAccion: tipoInfo.accion,
          };
        })
      );

      setMovimientos(movimientosConInfo);

    } catch (err) {
      setError(`❌ ${err.message}`);
      setMovimientos([]);
    } finally {
      setLoading(false);
    }
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
              onChange={(e) => setCuenta(e.target.value)}
              style={{ color: '#1e293b' }}
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Cargando...' : 'Consultar'}
          </button>
        </form>

        {movimientos.length > 0 && (
          <div style={{ marginTop: '1.5rem' }}>
            <h3>Movimientos de la cuenta {cuenta}:</h3>
            <table className="movements-table">
              <thead>
                <tr>
                  <th>NroMov</th>
                  <th>Cuenta</th>
                  <th>Fecha</th>
                  <th>Cód.</th>
                  <th>Tipo</th>
                  <th>Acción</th>
                  <th>Importe</th>
                </tr>
              </thead>
              <tbody>
                {movimientos.map((m, i) => (
                  <tr key={i}>
                    <td>{m.numero || '?'}</td>
                    <td>{m.cuentaCodigo || '?'}</td>
                    <td>{formatDate(m.fecha)}</td>
                    <td>{m.tipoCodigo || '?'}</td>
                    <td>{m.tipoDescripcion || '?'}</td>
                    <td>{m.tipoAccion || '?'}</td>
                    <td>{parseFloat(m.importe).toFixed(2).replace('.', ',')}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {movimientos.length === 0 && !loading && !error && (
          <p style={{ textAlign: 'center', color: '#64748b', marginTop: '1rem' }}>
            No hay movimientos registrados.
          </p>
        )}

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

export default ConsultarMovimientos;