import axios from 'axios';

// Usamos el proxy definido en package.json
const BASE_URL = '/ConUni_RESTFULL_Java_G10/webresources/servicio';

/**
 * Llama al servicio REST para convertir unidades.
 * @param {number} valor - Valor numérico a convertir
 * @param {string} tipo - Tipo de conversión (ej: "centimetroAmetros")
 * @returns {Promise<{ resultado: number, mensaje: string }>}
 */
export const convertirUnidad = async (valor, tipo) => {
    try {
        const response = await axios.post(
            `${BASE_URL}/conversionUnidades`,
            { valor, tipo },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        return response.data; // { resultado, mensaje }
    } catch (error) {
        console.error('Error al llamar al servicio REST:', error);
        // Si el error es de red o CORS, lanzamos un mensaje amigable
        if (error.code === 'ECONNABORTED' || !error.response) {
            throw new Error('Error de conexión. Verifique que el servidor esté ejecutándose.');
        } else {
            throw new Error(`Error del servidor: ${error.response?.status || 'desconocido'}`);
        }
    }
};