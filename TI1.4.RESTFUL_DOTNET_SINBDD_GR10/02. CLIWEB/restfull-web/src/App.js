import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Conversiones from './components/Conversiones';
import './App.css';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(
        sessionStorage.getItem('isLoggedIn') === 'true'
    );

    useEffect(() => {
        sessionStorage.setItem('isLoggedIn', isLoggedIn);
    }, [isLoggedIn]);

    const handleLogin = (success) => {
        if (success) {
            setIsLoggedIn(true);
        }
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
        sessionStorage.removeItem('isLoggedIn');
    };

    return (
        <div className="App">
            {isLoggedIn ? (
                <Conversiones onLogout={handleLogout} />
            ) : (
                <Login onLogin={handleLogin} />
            )}
        </div>
    );
}

export default App;