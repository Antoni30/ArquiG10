// src/pages/Menu.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

const Menu = () => {
  const navigate = useNavigate();

  const opciones = [
    { label: 'Consultar Movimientos', icon: '📈', path: '/movimientos' },
    { label: 'Realizar Depósito', icon: '💰', path: '/deposito' },
    { label: 'Transferencia', icon: '🔄', path: '/transferencia' },
    { label: 'Realizar Retiro', icon: '💸', path: '/retiro' },
    { label: 'Cerrar Sesión', icon: '🚪', path: '/login' },
  ];

  return (
    <Layout imageUrl="/images/menu-bg.png" imagePosition="right">
      <div className="form-card card-menu">
        <h2>🏦 Menú Principal</h2>
        <div className="menu-options">
          {opciones.map((opcion, i) => (
            <div
              key={i}
              className="menu-option"
              onClick={() => navigate(opcion.path)}
            >
              <span>{opcion.icon}</span>
              {opcion.label}
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
};

export default Menu;