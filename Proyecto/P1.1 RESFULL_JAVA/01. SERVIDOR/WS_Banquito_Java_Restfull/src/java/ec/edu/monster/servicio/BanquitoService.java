/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.servicio;

/**
 *
 * @author sdiegx
 */
import ec.edu.monster.db.DatabaseConnection;
import ec.edu.monster.modelo.Amortizacion;
import ec.edu.monster.modelo.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class BanquitoService {

    public Map<String, Object> procesarCompra(String cedula, double costo) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String sqlClienteCuenta = "SELECT cu.COD_CLIENTE, cu.NUM_CUENTA, cu.SALDO FROM CUENTA cu JOIN CLIENTE cl ON cu.COD_CLIENTE = cl.COD_CLIENTE WHERE cl.CEDULA = ?";

            PreparedStatement ps = con.prepareStatement(sqlClienteCuenta);
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return createResponse(false, "Cliente o cuenta no encontrada.");
            }

            int codCliente = rs.getInt("COD_CLIENTE");
            String numCuenta = rs.getString("NUM_CUENTA");
            double saldoActual = rs.getDouble("SALDO");

            if (costo > saldoActual) {
                return createResponse(false, "Saldo insuficiente en la cuenta.");
            }

            String sqlActualizarSaldo = "UPDATE CUENTA SET SALDO = SALDO - ? WHERE NUM_CUENTA = ?";
            ps = con.prepareStatement(sqlActualizarSaldo);
            ps.setDouble(1, costo);
            ps.setString(2, numCuenta);
            ps.executeUpdate();
            
            String sqlMaxMovimiento = "SELECT COALESCE(MAX(COD_MOVIMIENTO), 0) + 1 AS nuevoCodigo FROM MOVIMIENTO";
            int nuevoCodigoMovimiento = 0;
            ps = con.prepareStatement(sqlMaxMovimiento);
            ResultSet rsMax = ps.executeQuery();
            if (rsMax.next()) {
                nuevoCodigoMovimiento = rsMax.getInt("nuevoCodigo");
            }
            

            String sqlInsertarMovimiento = "INSERT INTO MOVIMIENTO (COD_CLIENTE, NUM_CUENTA, COD_MOVIMIENTO, TIPO, VALOR, FECHA) " +
                               "VALUES (?, ?, ?, 'RETIRO', ?, NOW())";
            ps = con.prepareStatement(sqlInsertarMovimiento);
            ps.setInt(1, codCliente);
            ps.setString(2, numCuenta);
            ps.setInt(3, nuevoCodigoMovimiento);
            ps.setDouble(4, costo);
            ps.executeUpdate();

            return createResponse(true, "Compra procesada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            return createResponse(false, "Error al procesar la compra: " + e.getMessage());
        }
    }

    public Cliente verificarClientePorCedula(String cedula) {
        String sql = "SELECT COD_CLIENTE, CEDULA, NOMBRE, GENERO, FECHA_NACIMIENTO FROM CLIENTE WHERE CEDULA = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodigo(rs.getInt("COD_CLIENTE"));
                cliente.setCedula(rs.getString("CEDULA"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setGenero(rs.getString("GENERO"));
                // Aquí realizamos la conversión
                cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                return cliente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean tieneDepositoReciente(int codCliente) {
        String sql = "SELECT COUNT(*) FROM MOVIMIENTO WHERE COD_CLIENTE = ? AND TIPO = 'DEPOSITO' AND FECHA >= CURRENT_DATE - INTERVAL '30' DAY";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, codCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean tieneCreditoActivo(int codCliente) {
        String sql = "SELECT COUNT(*) FROM CREDITO WHERE COD_CLIENTE = ? AND ESTADO = 'ACTIVO'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, codCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean esMayorDe25SiMasculino(int codCliente) {
        String sql = "SELECT FECHA_NACIMIENTO, GENERO FROM CLIENTE WHERE COD_CLIENTE = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, codCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate fechaNacimiento = rs.getDate("FECHA_NACIMIENTO").toLocalDate();
                String genero = rs.getString("GENERO");

                int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

                return !"M".equals(genero) || edad >= 25;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Map<String, Object> esSujetoDeCreditoConDetalles(String cedula) {
        Cliente cliente = verificarClientePorCedula(cedula);

        if (cliente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("sujetoDeCredito", false);
            response.put("razon", "El cliente no está registrado en el banco.");
            return response;
        }
        
        int codCliente = cliente.getCodigo();

        if (!tieneDepositoReciente(codCliente)) {
            Map<String, Object> response = new HashMap<>();
            response.put("sujetoDeCredito", false);
            response.put("razon", "El cliente no tiene transacciones de depósito en los últimos 30 días.");
            return response;
        }

        if (tieneCreditoActivo(codCliente)) {
            Map<String, Object> response = new HashMap<>();
            response.put("sujetoDeCredito", false);
            response.put("razon", "El cliente ya tiene un crédito activo.");
            return response;
        }

        if (!esMayorDe25SiMasculino(codCliente)) {
            Map<String, Object> response = new HashMap<>();
            response.put("sujetoDeCredito", false);
            response.put("razon", "El cliente es menor de 25 años y es masculino.");
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("sujetoDeCredito", true);
        response.put("razon", "El cliente cumple con todas las condiciones para ser sujeto de crédito.");
        return response;
    }


    public double calcularMontoMaximoCredito(String cedula) {
        int codCliente;

        String sqlObtenerCliente = "SELECT COD_CLIENTE FROM CLIENTE WHERE CEDULA = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlObtenerCliente)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                codCliente = rs.getInt("COD_CLIENTE");
            } else {
                return 0; // Cliente no encontrado
            }

            double promedioDepositos = calcularPromedio(codCliente, "DEPOSITO");
            double promedioRetiros = calcularPromedio(codCliente, "RETIRO");

            System.out.println(promedioDepositos);
             System.out.println(promedioRetiros);
             
            double montoMaximo = ((promedioDepositos - promedioRetiros) * 0.6) * 9;
            return Math.max(montoMaximo, 0); // Evitar valores negativos

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private double calcularPromedio(int codCliente, String tipo) throws SQLException {
        String sql = "SELECT AVG(VALOR) AS Promedio FROM MOVIMIENTO WHERE COD_CLIENTE = ? AND TIPO = ? AND FECHA >= CURRENT_DATE - INTERVAL '3' MONTH";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, codCliente);
            stmt.setString(2, tipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Promedio");
            }
        }
        return 0;
    }
    
    public Map<String, Object> asignarCredito(String cedula, double monto, int plazo) {
        try (Connection con = DatabaseConnection.getConnection()) {
            int codCliente;
            String numCuenta;

            // Obtener COD_CLIENTE y NUM_CUENTA
            String sqlObtenerCliente = "SELECT c.COD_CLIENTE, cu.NUM_CUENTA FROM CLIENTE c JOIN CUENTA cu ON c.COD_CLIENTE = cu.COD_CLIENTE WHERE c.CEDULA = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlObtenerCliente)) {
                stmt.setString(1, cedula);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    codCliente = rs.getInt("COD_CLIENTE");
                    numCuenta = rs.getString("NUM_CUENTA");
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("result", false);
                    response.put("status", "Cliente no encontrado.");
                    return response;
                }
            }
            
            double tasaInteres = 0;
            if (plazo<12){
                tasaInteres=16;
            }
            else if(tasaInteres<=24){
                tasaInteres=32;
            }
            // Insertar en la tabla CREDITO
            String sqlInsertarCredito = "INSERT INTO CREDITO (COD_CLIENTE, NUM_CUENTA, MONTO, TASA_INTERES, PLAZO_MESES, FECHA_INICIO, ESTADO) VALUES (?, ?, ?, ?, ?, CURRENT_DATE, 'ACTIVO')";
            try (PreparedStatement stmt = con.prepareStatement(sqlInsertarCredito)) {
                stmt.setInt(1, codCliente);
                stmt.setString(2, numCuenta);
                stmt.setDouble(3, monto);
                stmt.setDouble(4, tasaInteres);
                stmt.setInt(5, plazo);
                stmt.executeUpdate();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("result", true);
            response.put("status", "Crédito guardado con éxito.");
            return response;
        } catch (SQLException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("result", false);
            response.put("status", "No se pudo guardar el crédito. Error: " + e.getMessage());
            return response;
        }
    }

    public List<Amortizacion> generarTablaAmortizacion(String cedula) {
        List<Amortizacion> tabla = new ArrayList<>();

        String sql = "SELECT c.MONTO, c.PLAZO_MESES, c.TASA_INTERES " +
                     "FROM CREDITO c " +
                     "JOIN CLIENTE cl ON c.COD_CLIENTE = cl.COD_CLIENTE " +
                     "WHERE cl.CEDULA = ? AND c.ESTADO != 'CANCELADO'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double montoCredito = rs.getDouble("MONTO");
                int plazoMeses = rs.getInt("PLAZO_MESES");
                double tasaInteresAnual = rs.getDouble("TASA_INTERES");

                double tasaMensual = tasaInteresAnual / 12 / 100;
                double cuota = montoCredito * tasaMensual / (1 - Math.pow(1 + tasaMensual, -plazoMeses));
                double saldo = montoCredito;

                for (int i = 1; i <= plazoMeses; i++) {
                    double interes = saldo * tasaMensual;
                    double capital = cuota - interes;
                    saldo -= capital;
                    
                    tabla.add(new Amortizacion(i, Math.round(cuota * 100.0) / 100.0,
                                               Math.round(interes * 100.0) / 100.0,
                                               Math.round(capital * 100.0) / 100.0,
                                               Math.round(saldo * 100.0) / 100.0));
                }   
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tabla;
    }


    private Map<String, Object> createResponse(boolean result, String message) {
        return new HashMap<String, Object>() {{
            put("result", result);
            put("message", message);
        }};
    }
}

