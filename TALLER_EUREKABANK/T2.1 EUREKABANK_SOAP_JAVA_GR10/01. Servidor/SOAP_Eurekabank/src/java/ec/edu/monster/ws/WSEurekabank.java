
package ec.edu.monster.ws;

import ec.edu.monster.modelo.*;
import ec.edu.monster.db.AccesoDB;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebParam;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author rodri
 */
@WebService(serviceName = "WSEurekabank")
public class WSEurekabank {

     @WebMethod(operationName = "listarCuentas")
     public List<Cuenta> listar() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, " +
                     "chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, " +
                     "int_cuencontmov, chr_cuenclave FROM Cuenta";
        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cuenta(
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
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // en SOAP podrías lanzar un WebServiceException o devolver lista vacía
        }
        return lista;
    }
    
      @WebMethod
    public String hacerDeposito(@WebParam(name="req") MovimientoRequest req) {
        // Reusa exactamente la misma lógica que tenías en REST pero retorna String o código
        Connection cn = null;
        try {
            if (req == null || req.getCuentaCodigo() == null) return "Datos incompletos";
            if (req.getImporte() <= 0) return "Importe inválido";

            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            String sqlCuenta = "SELECT dec_cuensaldo, vch_cuenestado, int_cuencontmov FROM Cuenta WHERE chr_cuencodigo = ?";
            int nuevoNumero;
            try (PreparedStatement pstm = cn.prepareStatement(sqlCuenta)) {
                pstm.setString(1, req.getCuentaCodigo());
                try (ResultSet rs = pstm.executeQuery()) {
                    if (!rs.next()) { cn.rollback(); return "Cuenta no encontrada"; }
                    if (!"ACTIVO".equals(rs.getString("vch_cuenestado"))) { cn.rollback(); return "Cuenta no activa"; }
                    nuevoNumero = rs.getInt("int_cuencontmov") + 1;
                }
            }

             String sqlMov = "INSERT INTO Movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, " +
                           "chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) " +
                           "VALUES(?, ?, CURDATE(), ?, '003', ?, NULL)";
            try (PreparedStatement pstm = cn.prepareStatement(sqlMov)) {
                pstm.setString(1, req.getCuentaCodigo());
                pstm.setInt(2, nuevoNumero);
                pstm.setString(3, req.getEmpleadoCodigo());
                pstm.setBigDecimal(4, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.executeUpdate();
            }

            String sqlUpdate = "UPDATE Cuenta SET dec_cuensaldo = dec_cuensaldo + ?, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = ?";
            try (PreparedStatement pstm = cn.prepareStatement(sqlUpdate)) {
                pstm.setBigDecimal(1, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(2, req.getCuentaCodigo());
                pstm.executeUpdate();
            }

            cn.commit();
            return "OK";
        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        } finally {
            try { if (cn != null) { cn.setAutoCommit(true); cn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    @WebMethod
    public List<Movimiento> obtenerMovimientos(@WebParam(name="req") String cuentaCodigo){
          List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, " +
                     "chr_tipocodigo, dec_moviimporte, chr_cuenreferencia FROM Movimiento "+
                "WHERE chr_cuencodigo = ? ORDER BY dtt_movifecha DESC, int_movinumero DESC ";
        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, cuentaCodigo);
            try (ResultSet rs = pstm.executeQuery()) {
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
                    lista.add(m);
                }
            }
                return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
}

    @WebMethod
    public String hacerTransferencia(@WebParam(name="req")TransferenciaRequest req){
                if (req == null || 
            req.getCuentaOrigen() == null || 
            req.getCuentaDestino() == null || 
            req.getEmpleadoCodigo() == null) {
            return "Datos incompletos";
        }
        if (req.getImporte() <= 0) {
            return "El importe debe ser mayor a cero";
        }
        if (req.getCuentaOrigen().equals(req.getCuentaDestino())) {
            return "La cuenta origen y destino no pueden ser la misma";
        }

        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // 1. Verificar cuenta ORIGEN
            String sqlOrigen = "SELECT dec_cuensaldo, vch_cuenestado, int_cuencontmov FROM Cuenta WHERE chr_cuencodigo = ?";
            double saldoOrigen;
            int contadorOrigen;
            try (PreparedStatement pstm = cn.prepareStatement(sqlOrigen)) {
                pstm.setString(1, req.getCuentaOrigen());
                try (ResultSet rs = pstm.executeQuery()) {
                    if (!rs.next()) {
                        cn.rollback();
                        return "Cuenta origen no encontrada";
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return "La cuenta origen no está activa";
                    }
                    saldoOrigen = rs.getBigDecimal("dec_cuensaldo").doubleValue();
                    contadorOrigen = rs.getInt("int_cuencontmov");
                }
            }

            // 2. Verificar cuenta DESTINO
            String sqlDestino = "SELECT vch_cuenestado, int_cuencontmov FROM Cuenta WHERE chr_cuencodigo = ?";
            int contadorDestino;
            try (PreparedStatement pstm = cn.prepareStatement(sqlDestino)) {
                pstm.setString(1, req.getCuentaDestino());
                try (ResultSet rs = pstm.executeQuery()) {
                    if (!rs.next()) {
                        cn.rollback();
                        return "Cuenta destino no encontrada";
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return "La cuenta destino no está activa";
                    }
                    contadorDestino = rs.getInt("int_cuencontmov");
                }
            }

            // 3. Validar saldo suficiente en ORIGEN
            if (saldoOrigen < req.getImporte()) {
                cn.rollback();
                return "Saldo insuficiente en la cuenta origen";
            }

            // 4. Insertar movimiento de RETIRO en ORIGEN (tipo '009' = Transferencia SALIDA)
            String sqlMovOrigen = "INSERT INTO Movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, " +
                                 "chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) " +
                                 "VALUES(?, ?, CURDATE(), ?, '009', ?, ?)";
            try (PreparedStatement pstm = cn.prepareStatement(sqlMovOrigen)) {
                pstm.setString(1, req.getCuentaOrigen());
                pstm.setInt(2, contadorOrigen + 1);
                pstm.setString(3, req.getEmpleadoCodigo());
                pstm.setBigDecimal(4, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(5, req.getCuentaDestino()); // referencia
                pstm.executeUpdate();
            }

            // 5. Insertar movimiento de DEPÓSITO en DESTINO (tipo '008' = Transferencia INGRESO)
            String sqlMovDestino = "INSERT INTO Movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, " +
                                  "chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) " +
                                  "VALUES(?, ?, CURDATE(), ?, '008', ?, ?)";
            try (PreparedStatement pstm = cn.prepareStatement(sqlMovDestino)) {
                pstm.setString(1, req.getCuentaDestino());
                pstm.setInt(2, contadorDestino + 1);
                pstm.setString(3, req.getEmpleadoCodigo());
                pstm.setBigDecimal(4, java.math.BigDecimal.valueOf((long) req.getImporte()));
                pstm.setString(5, req.getCuentaOrigen()); // referencia
                pstm.executeUpdate();
            }

            // 6. Actualizar saldo y contador de ORIGEN
            String sqlUpdateOrigen = "UPDATE Cuenta SET dec_cuensaldo = dec_cuensaldo - ?, " +
                                    "int_cuencontmov = int_cuencontmov + 1 " +
                                    "WHERE chr_cuencodigo = ?";
            try (PreparedStatement pstm = cn.prepareStatement(sqlUpdateOrigen)) {
                pstm.setBigDecimal(1, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(2, req.getCuentaOrigen());
                pstm.executeUpdate();
            }

            // 7. Actualizar saldo y contador de DESTINO
            String sqlUpdateDestino = "UPDATE Cuenta SET dec_cuensaldo = dec_cuensaldo + ?, " +
                                     "int_cuencontmov = int_cuencontmov + 1 " +
                                     "WHERE chr_cuencodigo = ?";
            try (PreparedStatement pstm = cn.prepareStatement(sqlUpdateDestino)) {
                pstm.setBigDecimal(1, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(2, req.getCuentaDestino());
                pstm.executeUpdate();
            }

            cn.commit();
            return "Transferencia realizada exitosamente";

        } catch (SQLException e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                return "Error en la BD";
            }
            return "Error al realizar la transferencia: " + e.getMessage();
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
                if (cn != null) cn.close();
            } catch (SQLException e) {
                 return "Error al realizar la transferencia: " + e.getMessage();
            }
        }
    }

    @WebMethod
    public String hacerRetiro(@WebParam(name="req") MovimientoRequest req){
        if (req == null || req.getCuentaCodigo() == null || req.getEmpleadoCodigo() == null) {
            return "Datos incompletos";
        }
        if (req.getImporte() <= 0) {
            return "El importe debe ser mayor a cero";
        }

        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // 1. Verificar cuenta
            String sqlCuenta = "SELECT dec_cuensaldo, vch_cuenestado, int_cuencontmov FROM Cuenta WHERE chr_cuencodigo = ?";
            double saldoActual;
            int contadorActual;
            try (PreparedStatement pstm = cn.prepareStatement(sqlCuenta)) {
                pstm.setString(1, req.getCuentaCodigo());
                try (ResultSet rs = pstm.executeQuery()) {
                    if (!rs.next()) {
                        cn.rollback();
                        return "Cuenta no encontrada";
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return "La cuenta no está activa";
                    }
                    saldoActual = rs.getBigDecimal("dec_cuensaldo").doubleValue();
                    contadorActual = rs.getInt("int_cuencontmov");
                }
            }

            // 2. Validar saldo suficiente
            if (saldoActual < req.getImporte()) {
                cn.rollback();
                return "Saldo insuficiente";
            }

            // 3. Insertar movimiento (tipo '004' = Retiro)
            String sqlMov = "INSERT INTO Movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, " +
                           "chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) " +
                           "VALUES(?, ?, CURDATE(), ?, '004', ?, NULL)";
            try (PreparedStatement pstm = cn.prepareStatement(sqlMov)) {
                pstm.setString(1, req.getCuentaCodigo());
                pstm.setInt(2, contadorActual + 1);
                pstm.setString(3, req.getEmpleadoCodigo());
                pstm.setBigDecimal(4, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.executeUpdate();
            }

            // 4. Actualizar saldo y contador
            String sqlUpdate = "UPDATE Cuenta SET dec_cuensaldo = dec_cuensaldo - ?, " +
                              "int_cuencontmov = int_cuencontmov + 1 " +
                              "WHERE chr_cuencodigo = ?";
            try (PreparedStatement pstm = cn.prepareStatement(sqlUpdate)) {
                pstm.setBigDecimal(1, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(2, req.getCuentaCodigo());
                pstm.executeUpdate();
            }

            cn.commit();
            return "Retiro realizado exitosamente";

        } catch (Exception e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return "Error al realizar el retiro: " + e.getMessage();
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @WebMethod
    public TipoMovimiento tipoMoviento(@WebParam(name="req") String id){
        String sql = "SELECT chr_tipocodigo, vch_tipodescripcion, vch_tipoaccion, vch_tipoestado " +
                     "FROM TipoMovimiento WHERE chr_tipocodigo = ?";
        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    TipoMovimiento t = new TipoMovimiento(
                        rs.getString("chr_tipocodigo"),
                        rs.getString("vch_tipodescripcion"),
                        rs.getString("vch_tipoaccion"),
                        rs.getString("vch_tipoestado")
                    );
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TipoMovimiento();
    }
    
    @WebMethod
    public Cuenta buscarPorId(@WebParam(name="req") String id) {
        String sql = "SELECT chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, " +
                     "chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, " +
                     "int_cuencontmov, chr_cuenclave FROM Cuenta WHERE chr_cuencodigo = ?";
        try (Connection cn = AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
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
                    return c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @WebMethod
    public Empleado buscarPorIdEmpleado(@WebParam(name="req") String id) {
        String sql = "SELECT chr_emplcodigo, vch_emplpaterno, vch_emplmaterno, vch_emplnombre, " +
                     "vch_emplciudad, vch_empldireccion FROM Empleado WHERE chr_emplcodigo = ?";
        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Empleado e = new Empleado(
                        rs.getString("chr_emplcodigo"),
                        rs.getString("vch_emplpaterno"),
                        rs.getString("vch_emplmaterno"),
                        rs.getString("vch_emplnombre"),
                        rs.getString("vch_emplciudad"),
                        rs.getString("vch_empldireccion")
                    );
                    return e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
