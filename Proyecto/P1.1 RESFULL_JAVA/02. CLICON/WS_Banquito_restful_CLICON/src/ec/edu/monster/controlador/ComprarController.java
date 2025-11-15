/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.ElectrodomesticoSeleccionado;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ckan1
 */
public class ComprarController {
    private static final String BASE_URL = "http://localhost:8080/WS_Banquito_Java_Restfull/webresources/Banquito";
    private static final Gson gson = new Gson();
    private List<ElectrodomesticoSeleccionado> celularesSeleccionados = new ArrayList<>();

    public boolean esSujetoCredito(String cedula) throws IOException {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }

        URL url = new URL(BASE_URL + "/OtorgarCredito/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String content = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            conn.disconnect();
            JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();
            return jsonResponse.get("mensaje").getAsString().equalsIgnoreCase("El cliente es sujeto de crédito.");
        } else {
            conn.disconnect();
            return false;
        }
    }
    
     public String  razon(String cedula) throws IOException {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }

        URL url = new URL(BASE_URL + "/OtorgarCredito/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String content = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            conn.disconnect();
            JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();
            
            return jsonResponse.get("razon").getAsString();
        } else {
            conn.disconnect();
            return "No posee Razon";
        }
    }
    

    public double calcularMontoMaximo(String cedula) throws IOException {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }

        URL url = new URL(BASE_URL + "/CalcularMontoMaximo/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String content = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            conn.disconnect();
            JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();
            return jsonResponse.get("montoMaximo").getAsDouble();
        } else {
            conn.disconnect();
            throw new IOException("Error al calcular el monto máximo.");
        }
    }

    public boolean pagarCredito(String cedula, double monto) throws IOException {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }

        URL url = new URL(BASE_URL + "/ProcesarCompra");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = String.format("{\"cedula\":\"%s\", \"costo\":%s}", cedula, monto);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            return true;
        } else {
            conn.disconnect();
            return false;
        }
    }

    public void mostrarCompra(List<ElectrodomesticoSeleccionado> seleccionados) {
        // Actualiza la lista local para procesar en la interfaz
        this.celularesSeleccionados = seleccionados;
    }

    public boolean comprarCredito(String cedula, double monto,int credito) throws IOException{
            if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }
          URL url = new URL(BASE_URL + "/RegistrarCredito");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
          String json = String.format("{\"cedula\":\"%s\", \"monto\":\"%s\",\"plazo\":\"%s\"}", cedula, monto,credito);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            return true;
        } else {
            conn.disconnect();
            return false;
        }
    }
    public void incrementarContador(int id) {
        for (ElectrodomesticoSeleccionado celular : celularesSeleccionados) {
            if (celular.getId() == id) {
                celular.setContador(celular.getContador() + 1);
                break;
            }
        }
    }

    public void decrementarContador(int id) {
        for (ElectrodomesticoSeleccionado celular : celularesSeleccionados) {
            if (celular.getId() == id) {
                int nuevoContador = celular.getContador() - 1;
                celular.setContador(Math.max(1, nuevoContador)); // Restricción: No menor que 1
                break;
            }
        }
    }
    
    public Cliente datosCliente(String cedula) throws IOException{
        Cliente c = new Cliente();
        
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Cédula no proporcionada.");
        }

        URL url = new URL(BASE_URL + "/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String content = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            conn.disconnect();
            JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();
            c.setCedula(jsonResponse.get("cedula").getAsString());
            c.setNombre(jsonResponse.get("nombre").getAsString());
            return c;
        } else {
            conn.disconnect();
            throw new IOException("Error al calcular el monto máximo.");
        }
    }
}
