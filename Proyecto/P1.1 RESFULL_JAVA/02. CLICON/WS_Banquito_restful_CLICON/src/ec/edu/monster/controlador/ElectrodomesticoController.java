/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Electrodomestico;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ckan1
 */
public class ElectrodomesticoController {
    private static final String API_URL = "http://localhost:8080/WS_Banquito_Java_Restfull/webresources/Electrodomesticos";
    private static final Gson gson = new Gson();

    /**
     * Obtiene la lista de todos los electrodomesticos del servidor.
     *
     * @return Lista de objetos electrodomestico.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public List<Electrodomestico> obtenerElectrodomesticos() throws IOException {
        List<Electrodomestico> electrodomesticos = new ArrayList<>();
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }

                Type listType = new TypeToken<List<Electrodomestico>>() {}.getType();
                electrodomesticos = gson.fromJson(jsonResponse.toString(), listType);
            }
        } else {
            throw new IOException("Error al obtener los electrodomesticos: Código HTTP " + conn.getResponseCode());
        }

        conn.disconnect();
        return electrodomesticos;
    }

    /**
     * Edita un electrodomestico en el servidor.
     *
     * @param electrodomesticoEditado El electrodomestico con los datos actualizados.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public boolean editarElectrodomestico(Electrodomestico electrodomesticoEditado) throws IOException {
        URL url = new URL(API_URL + "/" + electrodomesticoEditado.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = gson.toJson(electrodomesticoEditado);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        boolean editado = (conn.getResponseCode() == 200 || conn.getResponseCode() == 204);
        conn.disconnect();
        return editado;
    }

    /**
     * Crea un nuevo electrodomestico en el servidor.
     *
     * @param nuevoelectrodomestico El electrodomestico a crear.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public boolean crearElectrodomestico(Electrodomestico nuevoelectrodomestico) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = gson.toJson(nuevoelectrodomestico);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        boolean creado = (conn.getResponseCode() == 200 || conn.getResponseCode() == 201);
        conn.disconnect();
        return creado;
    }

    /**
     * Elimina un electrodomestico en el servidor.
     *
     * @param id El ID del electrodomestico a eliminar.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public boolean eliminarElectrodomestico(int id) {
        try {
            URL url = new URL(API_URL + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            boolean eliminado = (conn.getResponseCode() == 200 || conn.getResponseCode() == 204);
            conn.disconnect();
            return eliminado;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
