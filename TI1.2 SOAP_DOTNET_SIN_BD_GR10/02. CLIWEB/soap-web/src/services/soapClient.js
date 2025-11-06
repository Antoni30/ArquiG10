import axios from 'axios';

const SOAP_URL = '/ConversionService.svc';
const NAMESPACE = 'http://tempuri.org/';
const SERVICE_INTERFACE = 'IConversionService'; // ← ¡Importante!

/**
 * Mapea el nombre del método al nombre del parámetro
 */
const getParamName = (methodName) => {
  const paramMap = {
    CentimetrosAMetros: 'centimetros',
    MetrosACentimetros: 'metros',
    MetrosAKilometros: 'metros',
    ToneladaALibra: 'tonelada',
    KilogramoALibra: 'kilogramo',
    GramoAKilogramo: 'gramos',
    CelsiusAFahrenheit: 'celsius',
    FahrenheitACelsius: 'fahrenheit',
    CelsiusAKelvin: 'celsius'
  };
  return paramMap[methodName] || 'value';
};

export const callSoapMethod = async (methodName, value) => {
  const paramName = getParamName(methodName);
  // SOAPAction CORRECTO para WCF: "http://tempuri.org/IConversionService/CentimetrosAMetros"
  const soapAction = `"${NAMESPACE}${SERVICE_INTERFACE}/${methodName}"`;

  const soapBody = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <${methodName} xmlns="${NAMESPACE}">
          <${paramName}>${value}</${paramName}>
        </${methodName}>
      </s:Body>
    </s:Envelope>
  `.trim();

  try {
    const response = await axios.post(SOAP_URL, soapBody, {
      headers: {
        'Content-Type': 'text/xml; charset=utf-8',
        'SOAPAction': soapAction // ← ¡Esta es la clave!
      }
    });

    // Extraer el resultado: <CentimetrosAMetrosResponse><CentimetrosAMetrosResult>2.22</CentimetrosAMetrosResult></CentimetrosAMetrosResponse>
    const xml = response.data;
    const resultTag = `<${methodName}Result>`;
    const start = xml.indexOf(resultTag) + resultTag.length;
    const end = xml.indexOf('</', start);
    
    if (start > -1 && end > start) {
      const result = xml.substring(start, end);
      return parseFloat(result);
    }

    throw new Error('No se encontró el resultado en la respuesta SOAP');
  } catch (error) {
    console.error('SOAP Error:', error);
    if (error.response) {
      console.error('Respuesta del servidor:', error.response.data);
    }
    throw new Error('Error al comunicarse con el servicio SOAP. Verifique que el servidor esté activo.');
  }
};