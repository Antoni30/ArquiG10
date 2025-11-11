// src/pages/Login.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

const Login = () => {
  const [usuario, setUsuario] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (usuario === 'MONSTER' && contrasena === 'MONSTER9') {
      navigate('/menu');
    } else {
      setError('❌ Credenciales incorrectas. Verifica tu usuario y contraseña.');
    }
  };

  return (
    <Layout imageUrl="/images/login-bg.png" imagePosition="left">
      <div className="form-card card-login">
        <h2>🔐 Inicio de Sesión</h2>
        {error && <div className="alert alert-error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Usuario</label>
            <input
              type="text"
              placeholder="Usuario"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              style={{ color: '#1e293b' }}
              autoComplete="username"
            />
          </div>
          <div className="form-group">
            <label>Contraseña</label>
            <input
              type="password"
              placeholder="Contraseña"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              style={{ color: '#1e293b' }}
              autoComplete="current-password"
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Ingresar
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default Login;