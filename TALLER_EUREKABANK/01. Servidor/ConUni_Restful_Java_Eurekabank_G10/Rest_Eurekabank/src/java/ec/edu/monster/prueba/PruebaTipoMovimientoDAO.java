package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.TipoMovimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaTipoMovimientoDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de tipos de movimiento...");
        List<TipoMovimiento> tipos = new ArrayList<>();
        String sql = "SELECT chr_tipocodigo, vch_tipodescripcion, vch_tipoaccion, vch_tipoestado " +
                     "FROM TipoMovimiento";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                TipoMovimiento t = new TipoMovimiento(
                    rs.getString("chr_tipocodigo"),
                    rs.getString("vch_tipodescripcion"),
                    rs.getString("vch_tipoaccion"),
                    rs.getString("vch_tipoestado")
                );
                tipos.add(t);
            }

            if (tipos.isEmpty()) {
                System.out.println("⚠️ No se encontraron tipos de movimiento.");
            } else {
                System.out.println("✅ Tipos de movimiento leídos correctamente:");
                for (TipoMovimiento t : tipos) {
                    System.out.println("   ➤ " + t.getCodigo() + " - " + t.getDescripcion() + " (" + t.getAccion() + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer tipos de movimiento:");
            e.printStackTrace();
        }
    }
}