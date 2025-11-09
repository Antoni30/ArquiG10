package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Moneda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaMonedaDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de monedas...");
        List<Moneda> monedas = new ArrayList<>();
        String sql = "SELECT chr_monecodigo, vch_monedescripcion FROM Moneda";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Moneda m = new Moneda(
                    rs.getString("chr_monecodigo"),
                    rs.getString("vch_monedescripcion")
                );
                monedas.add(m);
            }

            if (monedas.isEmpty()) {
                System.out.println("⚠️ No se encontraron monedas.");
            } else {
                System.out.println("✅ Monedas leídas correctamente:");
                for (Moneda m : monedas) {
                    System.out.println("   ➤ " + m.getCodigo() + " - " + m.getDescripcion());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer monedas:");
            e.printStackTrace();
        }
    }
}