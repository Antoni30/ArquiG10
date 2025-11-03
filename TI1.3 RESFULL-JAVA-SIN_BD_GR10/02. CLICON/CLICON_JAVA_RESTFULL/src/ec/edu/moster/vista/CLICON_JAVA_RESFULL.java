package ec.edu.moster.vista;

import ec.edu.monster.modelo.ServiceResponse;
import ec.edu.monster.utils.Login;
import ec.edu.monster.utils.Menu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLICON_JAVA_RESFULL {
    private static final String SERVICE_URL = "http://localhost:8080/ConUni_RESTFULL_Java_G10/webresources/servicio/conversionUnidades";

    public static void main(String[] args) throws IOException {

        Login login = new Login();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Menu men = new Menu();

        int opcion = 0;
        DecimalFormat df = new DecimalFormat("#.####"); // formatea a 4 decimales

        if (!login.login(br)) {
            System.out.println("\nDemasiados intentos. Programa finalizado.");
            return;
        }

        do {
            men.menu();
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Opción inválida. Intente nuevamente.");
                continue;
            }
            if (opcion == 0) {
                System.out.println("👋 Saliendo del programa...");
                break;
            }

            System.out.print("Ingrese el valor a convertir: ");
            double valor;
            try {
                valor = Double.parseDouble(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Valor inválido. Intente nuevamente.");
                continue;
            }

            try {
                String salida; // línea final a mostrar
                String tipoParaServicio = null;
                switch (opcion) {
                    case 1:
                        // Centímetros -> Metros
                        tipoParaServicio = "centimetroAmetros";
                        break;
                    case 2:
                        // Metros -> Centímetros
                        tipoParaServicio = "metrosAcentimetros";
                        break;
                    case 3:
                        // Metros -> Kilómetros
                        tipoParaServicio = "metrosAkilometros";
                        break;
                    case 4:
                        // Toneladas -> Libras
                        tipoParaServicio = "toneladasAlibras";
                        break;
                    case 5:
                        // Kilogramo -> Libra
                        tipoParaServicio = "kilogramosAlibras";
                        break;
                    case 6:
                        // Gramo -> Kilogramo
                        tipoParaServicio = "gramosAkilogramos";
                        break;
                    case 7:
                        // Celsius -> Fahrenheit
                        tipoParaServicio = "celciusAfahrenheit";
                        break;
                    case 8:
                        // Fahrenheit -> Celsius
                        tipoParaServicio = "fahrenheitAcelsius";
                        break;
                    case 9:
                        // Celsius -> Kelvin
                        // corregí posible typo "kerlvin" -> "kelvin"
                        tipoParaServicio = "celsiusAkelvin";
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        continue;
                }

                // Llamar al servicio y obtener respuesta estructurada
                ServiceResponse resp = callService(valor, tipoParaServicio);

                if (resp == null) {
                    System.out.println("⚠️ Error: No se obtuvo respuesta del servicio.");
                    continue;
                }

                // Si el servicio devolvió resultado -1 -> mostrar mensaje del servidor (si existe)
                if (Double.compare(resp.getResultado(), -1.0) == 0) {
                    if (resp.getMensaje() != null && !resp.getMensaje().isEmpty()) {
                        System.out.println("❌ Respuesta del servidor: " + resp.getMensaje());
                    } else {
                        System.out.println("❌ Respuesta del servidor: resultado = -1 (error desconocido)");
                    }
                    continue;
                }

                // Si llegamos aquí, tenemos un resultado válido; mostrar según la opción
                switch (opcion) {
                    case 1:
                        salida = df.format(valor) + " cm  ->  " + df.format(resp.getResultado()) + " m";
                        break;
                    case 2:
                        salida = df.format(valor) + " m   ->  " + df.format(resp.getResultado()) + " cm";
                        break;
                    case 3:
                        salida = df.format(valor) + " m   ->  " + df.format(resp.getResultado()) + " km";
                        break;
                    case 4:
                        salida = df.format(valor) + " t   ->  " + df.format(resp.getResultado()) + " lb";
                        break;
                    case 5:
                        salida = df.format(valor) + " kg  ->  " + df.format(resp.getResultado()) + " lb";
                        break;
                    case 6:
                        salida = df.format(valor) + " g   ->  " + df.format(resp.getResultado()) + " kg";
                        break;
                    case 7:
                        salida = df.format(valor) + " °C  ->  " + df.format(resp.getResultado()) + " °F";
                        break;
                    case 8:
                        salida = df.format(valor) + " °F  ->  " + df.format(resp.getResultado()) + " °C";
                        break;
                    case 9:
                        salida = df.format(valor) + " °C  ->  " + df.format(resp.getResultado()) + " K";
                        break;
                    default:
                        salida = "Resultado: " + df.format(resp.getResultado());
                }
                System.out.println("✅ Resultado: " + salida);

            } catch (Exception ex) {
                System.out.println("⚠️ Error llamando al servicio: " + ex.getMessage());
                ex.printStackTrace();
            }

        } while (opcion != 0);

    }

    /**
     * Llama al servicio REST enviando un JSON { "valor": <double>, "tipo": "<string>" }
     * y retorna un ServiceResponse con 'resultado' y 'mensaje'.
     * Si ocurre un error de comunicación devuelve null.
     */
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
