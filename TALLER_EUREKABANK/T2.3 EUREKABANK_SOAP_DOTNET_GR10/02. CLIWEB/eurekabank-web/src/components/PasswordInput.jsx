// src/components/PasswordInput.jsx
import React, { useState } from 'react';

const PasswordInput = ({ value, onChange, placeholder = "Contraseña" }) => {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div style={{ position: 'relative', width: '100%' }}>
      <input
        type={showPassword ? 'text' : 'password'}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        style={{
          width: '100%',
          padding: '0.75rem',
          fontSize: '1rem',
          border: '1px solid #ccc',
          borderRadius: '4px',
        }}
      />
      <button
        type="button"
        onClick={() => setShowPassword(!showPassword)}
        style={{
          position: 'absolute',
          right: '10px',
          top: '50%',
          transform: 'translateY(-50%)',
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          color: '#6b7280',
        }}
      >
        {showPassword ? 'Ocultar' : 'Mostrar'}
      </button>
    </div>
  );
};

export default PasswordInput;