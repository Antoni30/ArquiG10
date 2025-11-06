import React, { useState } from 'react';

const Login = ({ onLogin }) => {
    const [usuario, setUsuario] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        const USUARIO_CORRECTO = "MONSTER";
        const CLAVE_CORRECTA = "MONSTER9";

        if (usuario === USUARIO_CORRECTO && password === CLAVE_CORRECTA) {
            setError('');
            onLogin(true);
        } else {
            setError('Credenciales incorrectas. Intente de nuevo.');
        }
    };

    return (
        <div className="login-main-container"> 
            <div className="login-form-section"> 
                <h2>Inicio de Sesión</h2>
                {error && <p className="message result-error">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Usuario:</label>
                        <input type="text" value={usuario} onChange={(e) => setUsuario(e.target.value)} required />
                    </div>
                    <div>
                        <label>Contraseña:</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit">Iniciar Sesión</button>
                </form>
            </div>
            <div className="login-image-section">
                <img src="/Monster.jpg" alt="Escudo de Liga Deportiva Universitaria de Quito" />
            </div>
        </div>
    );
};

export default Login;