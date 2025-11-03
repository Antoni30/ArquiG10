
package ec.edu.monster.modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author rodri
 */
public class ConversionUnidades {
         private static final String SERVICE_URL = "http://localhost:8080/ConUni_RESTFULL_Java_G10/webresources/servicio/conversionUnidades";
        
        public double centimetrosAMetros(double centimetros) {
            ServiceResponse res = callService(centimetros, "centimetroAmetros");
             return   res.getResultado();
        }

    public double metrosACentimetros(double metros) {
            ServiceResponse res = callService(metros, "metrosAcentimetros");
             return   res.getResultado();
    }


    public double metrosAKilometros(double metros) {
            ServiceResponse res = callService(metros, "metrosAkilometros");
            return   res.getResultado();
    }

    public double toneladaALibra(double tonelada) {
           ServiceResponse res = callService(tonelada, "toneladasAlibras");
            return   res.getResultado();
    }


    public double kilogramoALibra(double kilogramo) {
            ServiceResponse res = callService(kilogramo, "kilogramosAlibras");
            return   res.getResultado();
    }


    public double gramoAKilogramo(double gramos) {
            ServiceResponse res = callService(gramos, "gramosAkilogramos");
            return   res.getResultado();
    }

    public double celsiusAFahrenheit(double celsius) {
            ServiceResponse res = callService(celsius, "celciusAfahrenheit");
            return   res.getResultado();
    }

    public double fahrenheitACelsius(double fahrenheit) {
            ServiceResponse res = callService(fahrenheit, "fahrenheitAcelsius");
            return   res.getResultado();
    }

    public double celsiusAKelvin(double celsius) {
               ServiceResponse res = callService(celsius, "celsiusAkelvin");
            return   res.getResultado();
    }
    
      private static ServiceResponse callService(double valor, String tipo) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            // Construir JSON manualmente (no depende de librerías externas)
            String jsonBody = String.format("{\"valor\":%s,\"tipo\":\"%s\"}", Double.toString(valor), tipo);

            // Enviar body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("UTF-8");
                os.write(input, 0, input.length);
                os.flush();
            }

            int status = conn.getResponseCode();
            BufferedReader in;
            if (status >= 200 && status < 300) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                // Leer mensaje de error si el status no es 2xx
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream(), "UTF-8"));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String resp = response.toString().trim();

            // Intentar extraer "resultado": <number> y "mensaje": "<text>" del JSON simple
            Double resultado = null;
            String mensaje = null;

            // Buscar "resultado": número (ej: "resultado":-1.0 o resultado: -1)
            Pattern pResultado = Pattern.compile("\"resultado\"\\s*:\\s*([-]?\\d+\\.?\\d*)");
            Matcher mResultado = pResultado.matcher(resp);
            if (mResultado.find()) {
                try {
                    resultado = Double.parseDouble(mResultado.group(1));
                } catch (NumberFormatException ex) {
                    resultado = null;
                }
            }

            // Buscar "mensaje": "texto"
            Pattern pMensaje = Pattern.compile("\"mensaje\"\\s*:\\s*\"([^\"]*)\"");
            Matcher mMensaje = pMensaje.matcher(resp);
            if (mMensaje.find()) {
                mensaje = mMensaje.group(1);
            }

            // Si no encontramos resultado intentando una respuesta directa numérica:
            if (resultado == null) {
                // intentar parsear la respuesta completa como número limpio (por compatibilidad con respuestas antiguas)
                String cleaned = resp;
                if ((cleaned.startsWith("\"") && cleaned.endsWith("\"")) || (cleaned.startsWith("'") && cleaned.endsWith("'"))) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                }
                try {
                    resultado = Double.parseDouble(cleaned);
                } catch (NumberFormatException ex) {
                    // No es un número: si había mensaje devolver resultado = -1 para indicar error
                    resultado = -1.0;
                }
            }

            // Devolver respuesta
            return new ServiceResponse(resultado, mensaje);

        } catch (Exception e) {
            System.err.println("Excepción en callService: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
