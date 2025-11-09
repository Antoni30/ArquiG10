package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Movimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaMovimientoDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de movimientos...");
        List<Movimiento> movs = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, " +
                     "chr_tipocodigo, dec_moviimporte, chr_cuenreferencia FROM Movimiento LIMIT 3";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Movimiento m = new Movimiento(
                    rs.getString("chr_cuencodigo"),
                    rs.getInt("int_movinumero"),
                    rs.getDate("dtt_movifecha").toString(),
                    rs.getString("chr_emplcodigo"),
                    rs.getString("chr_tipocodigo"),
                    rs.getBigDecimal("dec_moviimporte").doubleValue(),
                    rs.getString("chr_cuenreferencia")
                );
                movs.add(m);
            }

            if (movs.isEmpty()) {
                System.out.println("⚠️ No se encontraron movimientos.");
            } else {
                System.out.println("✅ Movimientos leídos correctamente:");
                for (Movimiento m : movs) {
                    System.out.println("   ➤ Cuenta: " + m.getCuentaCodigo() + " - Importe: " + m.getImporte() + " - Tipo: " + m.getTipoCodigo());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer movimientos:");
            e.printStackTrace();
        }
    }
}