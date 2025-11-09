package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaEmpleadoDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de empleados...");
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT chr_emplcodigo, vch_emplpaterno, vch_emplmaterno, vch_emplnombre, " +
                     "vch_emplciudad, vch_empldireccion FROM Empleado LIMIT 3";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado(
                    rs.getString("chr_emplcodigo"),
                    rs.getString("vch_emplpaterno"),
                    rs.getString("vch_emplmaterno"),
                    rs.getString("vch_emplnombre"),
                    rs.getString("vch_emplciudad"),
                    rs.getString("vch_empldireccion")
                );
                empleados.add(e);
            }

            if (empleados.isEmpty()) {
                System.out.println("⚠️ No se encontraron empleados.");
            } else {
                System.out.println("✅ Empleados leídos correctamente:");
                for (Empleado e : empleados) {
                    System.out.println("   ➤ " + e.getCodigo() + " - " + e.getNombre() + " " + e.getPaterno());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer empleados:");
            e.printStackTrace();
        }
    }
}