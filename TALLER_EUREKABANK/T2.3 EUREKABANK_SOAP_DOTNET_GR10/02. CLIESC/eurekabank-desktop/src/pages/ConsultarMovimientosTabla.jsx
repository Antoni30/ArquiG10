// src/pages/ConsultarMovimientosTabla.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ConsultarMovimientosTabla = () => {
  const { cuenta } = useParams();
  const [movimientos, setMovimientos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const [year, month, day] = dateStr.split('-');
    return `${day}/${month}/${year}`;
  };

  const getTipoInfo = async (tipoCodigo) => {
    try {
      const res = await api.get(`/tiposmovimiento/${tipoCodigo}`);
      return {
        descripcion: res.data?.descripcion || tipoCodigo,
        accion: res.data?.accion || '?',
      };
    } catch {
      return { descripcion: tipoCodigo, accion: '?' };
    }
  };

  useEffect(() => {
    const fetchMovimientos = async () => {
      try {
        // Validar existencia de la cuenta
        let cuentaExiste = false;
        try {
          const res = await api.get(`/cuentas/${cuenta}`);
          cuentaExiste = res.data != null;
        } catch {
          cuentaExiste = false;
        }
        if (!cuentaExiste) {
          setError(`❌ La cuenta "${cuenta}" no existe.`);
          setMovimientos([]);
          return;
        }

        const res = await api.get(`/cuentas/${cuenta}/movimientos`);
        let data = Array.isArray(res.data) ? res.data : [];

        // Ordenar por numero ascendente
        data.sort((a, b) => a.numero - b.numero);

        // Obtener info de tipo
        const movConInfo = await Promise.all(
          data.map(async (m) => {
            const info = await getTipoInfo(m.tipoCodigo);
            return { ...m, tipoDescripcion: info.descripcion, tipoAccion: info.accion };
          })
        );

        setMovimientos(movConInfo);
      } catch (err) {
        setError(`❌ Error al cargar movimientos: ${err.message}`);
        setMovimientos([]);
      } finally {
        setLoading(false);
      }
    };

    fetchMovimientos();
  }, [cuenta]);

  return (
    <div className='formulario'
    >
      {/* Contenedor de la tabla */}
      <div
        style={{
          width: '100%',
          maxWidth: '1200px',
          padding: '2rem',
          margin: 'auto',
          backgroundColor: '#ffffff',
          borderRadius: '16px',
          boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
          boxSizing: 'border-box',
        }}
      >
        {/* Encabezado */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '1rem',
          borderBottom: '2px solid #e2e8f0',
          paddingBottom: '0.5rem',
        }}>
          <span style={{ fontSize: '1.5rem', color: '#0d9488' }}>📈</span>
          <h2 style={{
            fontSize: '1.5rem',
            fontWeight: '600',
            color: '#0d9488',
            margin: 0,
          }}>
            Movimientos - Cuenta {cuenta}
          </h2>
        </div>

        {/* Mensaje de error */}
        {error && (
          <div
            style={{
              padding: '1rem',
              borderRadius: '10px',
              marginBottom: '1.2rem',
              backgroundColor: '#fee2e2',
              color: '#dc2626',
              borderLeft: '4px solid #dc2626',
              display: 'flex',
              alignItems: 'center',
              gap: '0.6rem',
            }}
          >
            {error}
          </div>
        )}

        {/* Cargando */}
        {loading ? (
          <p>Cargando movimientos...</p>
        ) : movimientos.length > 0 ? (
          <div style={{ width: '100%', overflowX: 'auto' }}>
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                fontSize: '0.95rem',
              }}
            >
              <thead>
                <tr>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    NroMov
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Cuenta
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Fecha
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Cód.
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Tipo
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Acción
                  </th>
                  <th style={{ padding: '0.75rem', backgroundColor: '#f1f5f9', fontWeight: '600', textAlign: 'left' }}>
                    Importe
                  </th>
                </tr>
              </thead>
              <tbody>
                {movimientos.map((m, i) => (
                  <tr key={i} style={{ borderBottom: '1px solid #cbd5e1' }}>
                    <td style={{ padding: '0.75rem' }}>{m.numero || '?'}</td>
                    <td style={{ padding: '0.75rem' }}>{m.cuentaCodigo || '?'}</td>
                    <td style={{ padding: '0.75rem' }}>{formatDate(m.fecha)}</td>
                    <td style={{ padding: '0.75rem' }}>{m.tipoCodigo || '?'}</td>
                    <td style={{ padding: '0.75rem' }}>{m.tipoDescripcion || '?'}</td>
                    <td style={{ padding: '0.75rem' }}>{m.tipoAccion || '?'}</td>
                    <td style={{ padding: '0.75rem' }}>{parseFloat(m.importe).toFixed(2).replace('.', ',')}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          !loading && <p>No hay movimientos registrados.</p>
        )}

        {/* Botones */}
        <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem', width: '100%' }}>
          <button
            onClick={() => navigate('/movimientos')}
            style={{
              flex: 1,
              padding: '0.9rem',
              fontSize: '1.05rem',
              fontWeight: '600',
              border: 'none',
              borderRadius: '10px',
              backgroundColor: '#1e40af',
              color: 'white',
              cursor: 'pointer',
            }}
          >
            🔍 Consultar otra cuenta
          </button>
          <button
            onClick={() => navigate('/menu')}
            style={{
              flex: 1,
              padding: '0.9rem',
              fontSize: '1.05rem',
              fontWeight: '600',
              border: 'none',
              borderRadius: '10px',
              backgroundColor: '#1e40af',
              color: 'white',
              cursor: 'pointer',
            }}
          >
            ⬅️ Atrás al Menú
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConsultarMovimientosTabla;