import axios from 'axios';

// URL del Web Service de Conversiones (usará el proxy en desarrollo)
const ENDPOINT_URL = '/ConUni_Soap_Java_G10/WSConversionUnidades';
const TARGET_NAMESPACE = 'http://ws.moster.edu.ec/'; // Namespace de tu WSConversionUnidades

/**
 * Función genérica para llamar al servicio SOAP, construyendo y analizando el XML.
 */
const callSoapService = async (methodName, paramName, value) => {
    // 1. CONSTRUCCIÓN DEL ENVELOPE XML
    const soapRequest = `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                          xmlns:ws="${TARGET_NAMESPACE}">
            <soapenv:Header/>
            <soapenv:Body>
                <ws:${methodName}>
                    <${paramName}>${value}</${paramName}>
                </ws:${methodName}>
            </soapenv:Body>
        </soapenv:Envelope>
    `;

    try {
        const response = await axios({
            method: 'post',
            url: ENDPOINT_URL,
            data: soapRequest,
            headers: {
                'Content-Type': 'text/xml;charset=UTF-8',
                'SOAPAction': '' // Necesario para JAX-WS en algunos casos
            },
        });

        // 2. PARSEO DE LA RESPUESTA XML
        const xmlResponse = response.data;
        // Expresión regular para extraer el contenido numérico dentro del tag <return>
        const resultTag = `\\<return\\>([\\d\\.\\-]+)\\<\\/return\\>`;
        
        const regex = new RegExp(resultTag, 's');
        const match = regex.exec(xmlResponse);

        if (match && match[1]) {
            return parseFloat(match[1]);
        }
        
        throw new Error('No se pudo analizar la respuesta SOAP o el resultado es nulo.');

    } catch (error) {
        console.error('Error al llamar al servicio SOAP:', error);
        // Lanza un error genérico para la UI
        throw new Error('Error de conexión. Verifique que el servidor esté ejecutándose.');
    }
};

export default callSoapService;