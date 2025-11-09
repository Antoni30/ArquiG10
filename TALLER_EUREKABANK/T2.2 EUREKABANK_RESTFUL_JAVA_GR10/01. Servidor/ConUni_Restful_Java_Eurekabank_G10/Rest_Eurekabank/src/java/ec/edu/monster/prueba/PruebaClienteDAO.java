package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaClienteDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de clientes...");
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT chr_cliecodigo, vch_cliepaterno, vch_cliematerno, vch_clienombre, " +
                     "chr_cliedni, vch_clieciudad, vch_cliedireccion, vch_clietelefono, vch_clieemail " +
                     "FROM Cliente LIMIT 3";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("chr_cliecodigo"),
                    rs.getString("vch_cliepaterno"),
                    rs.getString("vch_cliematerno"),
                    rs.getString("vch_clienombre"),
                    rs.getString("chr_cliedni"),
                    rs.getString("vch_clieciudad"),
                    rs.getString("vch_cliedireccion"),
                    rs.getString("vch_clietelefono"),
                    rs.getString("vch_clieemail")
                );
                clientes.add(c);
            }

            if (clientes.isEmpty()) {
                System.out.println("⚠️ No se encontraron clientes.");
            } else {
                System.out.println("✅ Clientes leídos correctamente:");
                for (Cliente c : clientes) {
                    System.out.println("   ➤ " + c.getCodigo() + " - " + c.getNombre() + " " + c.getPaterno());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer clientes:");
            e.printStackTrace();
        }
    }
}