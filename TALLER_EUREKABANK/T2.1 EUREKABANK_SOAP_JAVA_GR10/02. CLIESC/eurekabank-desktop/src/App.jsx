// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Menu from './pages/Menu';
import ConsultarMovimientosForm from './pages/ConsultarMovimientosForm';
import ConsultarMovimientosTabla from './pages/ConsultarMovimientosTabla'; // ← nueva
import RealizarDeposito from './pages/RealizarDeposito';
import RealizarRetiro from './pages/RealizarRetiro';
import RealizarTransferencia from './pages/RealizarTransferencia';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/movimientos" element={<ConsultarMovimientosForm />} />
        <Route path="/movimientos/:cuenta" element={<ConsultarMovimientosTabla />} /> {/* ← nueva */}
        <Route path="/deposito" element={<RealizarDeposito />} />
        <Route path="/retiro" element={<RealizarRetiro />} />
        <Route path="/transferencia" element={<RealizarTransferencia />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;