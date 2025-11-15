/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Amortizacion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ckan1
 */
public class ConsultarAmortizacionController {
    private static final String API_URL = "http://localhost:8080/WS_Banquito_Java_Restfull/webresources/Banquito/GenerarTablaAmortizacion/";

    /**
     * Método para generar la tabla de amortización desde el servicio REST.
     *
     * @param cedula La cédula del cliente para consultar la tabla.
     * @return Lista de objetos Amortizacion.
     * @throws Exception Si ocurre un error durante la conexión o el procesamiento.
     */
    public List<Amortizacion> generarTablaAmortizacion(String cedula) throws Exception {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria.");
        }

        List<Amortizacion> tablaAmortizacion = new ArrayList<>();

        try {
            // Construir la URL para la solicitud
            URL url = new URL(API_URL + cedula);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Verificar el código de respuesta
            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // Leer la respuesta JSON
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }
                in.close();

                // Convertir la respuesta JSON a una lista de objetos Amortizacion
                String jsonResponse = responseBuilder.toString();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Amortizacion>>() {}.getType();
                tablaAmortizacion = gson.fromJson(jsonResponse, listType);

            } else {
                throw new Exception("Error al obtener la tabla de amortización. Código HTTP: " + status);
            }
        } catch (Exception e) {
            throw new Exception("Error al conectar con el servidor: " + e.getMessage(), e);
        }

        return tablaAmortizacion;
    }
}
