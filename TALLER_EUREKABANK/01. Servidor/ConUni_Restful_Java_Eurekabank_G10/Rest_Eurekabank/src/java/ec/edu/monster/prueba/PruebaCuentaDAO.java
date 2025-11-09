package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Cuenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaCuentaDAO {
    public static void main(String[] args) {
        System.out.println("🔍 Probando lectura de cuentas...");
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, " +
                     "chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, " +
                     "int_cuencontmov, chr_cuenclave FROM Cuenta LIMIT 3";

        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Cuenta c = new Cuenta(
                    rs.getString("chr_cuencodigo"),
                    rs.getString("chr_monecodigo"),
                    rs.getString("chr_sucucodigo"),
                    rs.getString("chr_emplcreacuenta"),
                    rs.getString("chr_cliecodigo"),
                    rs.getBigDecimal("dec_cuensaldo").doubleValue(),
                    rs.getDate("dtt_cuenfechacreacion").toString(),
                    rs.getString("vch_cuenestado"),
                    rs.getInt("int_cuencontmov"),
                    rs.getString("chr_cuenclave")
                );
                cuentas.add(c);
            }

            if (cuentas.isEmpty()) {
                System.out.println("⚠️ No se encontraron cuentas.");
            } else {
                System.out.println("✅ Cuentas leídas correctamente:");
                for (Cuenta c : cuentas) {
                    System.out.println("   ➤ " + c.getCodigo() + " - Saldo: " + c.getSaldo() + " - Estado: " + c.getEstado());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al leer cuentas:");
            e.printStackTrace();
        }
    }
}