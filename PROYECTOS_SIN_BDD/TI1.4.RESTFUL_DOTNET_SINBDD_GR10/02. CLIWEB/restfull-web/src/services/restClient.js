import axios from 'axios';

// Usa HTTP, puerto 5052 (el que sí funciona)
const BASE_URL = '/api/conversion';

export const convertirUnidad = async (valor, tipo) => {
    try {
        const response = await axios.post(
            `${BASE_URL}/${tipo}`,
            parseFloat(valor), // 👈 Envía directamente el número (no un objeto, no un string)
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        // Tu API devuelve { success, message, data, timestamp }
        if (response.data?.Success === true || response.data?.success === true) {
            return {
                resultado: response.data.Data || response.data.data,
                mensaje: response.data.Message || response.data.message
            };
        } else {
            throw new Error(response.data?.Message || response.data?.message || 'Error desconocido del servidor');
        }
    } catch (error) {
        console.error('Error en la conversión:', error);
        if (!error.response) {
            throw new Error('Error de conexión. Verifique que el servidor esté ejecutándose.');
        } else {
            // Error 500: casi siempre significa que el cuerpo no era un número válido
            if (error.response.status === 500) {
                throw new Error('Error del servidor: el valor enviado no es un número válido. Intente nuevamente.');
            }
            throw new Error(`Error ${error.response.status}: ${error.response.data?.message || error.message}`);
        }
    }
};