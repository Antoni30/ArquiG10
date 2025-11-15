package ec.edu.monster.ws;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Cuenta;
import ec.edu.monster.modelo.Movimiento;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuentaWS {

    // ========== CONSULTAS (GET) ==========

    @GET
    public Response listar() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, " +
                     "chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, " +
                     "int_cuencontmov, chr_cuenclave FROM Cuenta";

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
                lista.add(c);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar cuentas").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
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
                    return Response.ok(c).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("Cuenta no encontrada").build();
    }

    // ========== DEPÓSITO ==========

    @POST
    @Path("/deposito")
    public Response hacerDeposito(MovimientoRequest req) {
        if (req == null || req.getCuentaCodigo() == null || req.getEmpleadoCodigo() == null) {
            return Response.status(400).entity("Datos incompletos").build();
        }
        if (req.getImporte() <= 0) {
            return Response.status(400).entity("El importe debe ser mayor a cero").build();
        }

        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // 1. Verificar que la cuenta exista y esté ACTIVA
            String sqlCuenta = "SELECT dec_cuensaldo, vch_cuenestado, int_cuencontmov FROM Cuenta WHERE chr_cuencodigo = ?";
            Cuenta cuenta;
            try (PreparedStatement pstm = cn.prepareStatement(sqlCuenta)) {
                pstm.setString(1, req.getCuentaCodigo());
                try (ResultSet rs = pstm.executeQuery()) {
                    if (!rs.next()) {
                        cn.rollback();
                        return Response.status(404).entity("Cuenta no encontrada").build();
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return Response.status(400).entity("La cuenta no está activa").build();
                    }
                    cuenta = new Cuenta();
                    cuenta.setSaldo(rs.getBigDecimal("dec_cuensaldo").doubleValue());
                    cuenta.setContadorMovimientos(rs.getInt("int_cuencontmov"));
                }
            }

            // 2. Obtener el siguiente número de movimiento
            int nuevoNumero = cuenta.getContadorMovimientos() + 1;

            // 3. Insertar movimiento (tipo '003' = Depósito)
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

            // 4. Actualizar saldo y contador
            String sqlUpdate = "UPDATE Cuenta SET dec_cuensaldo = dec_cuensaldo + ?, " +
                              "int_cuencontmov = int_cuencontmov + 1 " +
                              "WHERE chr_cuencodigo = ?";
            try (PreparedStatement pstm = cn.prepareStatement(sqlUpdate)) {
                pstm.setBigDecimal(1, java.math.BigDecimal.valueOf(req.getImporte()));
                pstm.setString(2, req.getCuentaCodigo());
                pstm.executeUpdate();
            }

            cn.commit();
            return Response.ok("Depósito realizado exitosamente").build();

        } catch (Exception e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return Response.status(500).entity("Error al realizar el depósito: " + e.getMessage()).build();
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ========== RETIRO ==========

    @POST
    @Path("/retiro")
    public Response hacerRetiro(MovimientoRequest req) {
        if (req == null || req.getCuentaCodigo() == null || req.getEmpleadoCodigo() == null) {
            return Response.status(400).entity("Datos incompletos").build();
        }
        if (req.getImporte() <= 0) {
            return Response.status(400).entity("El importe debe ser mayor a cero").build();
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
                        return Response.status(404).entity("Cuenta no encontrada").build();
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return Response.status(400).entity("La cuenta no está activa").build();
                    }
                    saldoActual = rs.getBigDecimal("dec_cuensaldo").doubleValue();
                    contadorActual = rs.getInt("int_cuencontmov");
                }
            }

            // 2. Validar saldo suficiente
            if (saldoActual < req.getImporte()) {
                cn.rollback();
                return Response.status(400).entity("Saldo insuficiente").build();
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
            return Response.ok("Retiro realizado exitosamente").build();

        } catch (Exception e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return Response.status(500).entity("Error al realizar el retiro: " + e.getMessage()).build();
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ========== CONSULTAR MOVIMIENTOS DE UNA CUENTA ==========

    @GET
    @Path("/{cuentaCodigo}/movimientos")
    public Response obtenerMovimientos(@PathParam("cuentaCodigo") String cuentaCodigo) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, " +
                     "chr_tipocodigo, dec_moviimporte, chr_cuenreferencia FROM Movimiento " +
                     "WHERE chr_cuencodigo = ? ";

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
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al obtener movimientos").build();
        }
    }
    
    // ========== TRANSFERENCIA ==========
    @POST
    @Path("/transferencia")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hacerTransferencia(TransferenciaRequest req) {
        if (req == null || 
            req.getCuentaOrigen() == null || 
            req.getCuentaDestino() == null || 
            req.getEmpleadoCodigo() == null) {
            return Response.status(400).entity("Datos incompletos").build();
        }
        if (req.getImporte() <= 0) {
            return Response.status(400).entity("El importe debe ser mayor a cero").build();
        }
        if (req.getCuentaOrigen().equals(req.getCuentaDestino())) {
            return Response.status(400).entity("La cuenta origen y destino no pueden ser la misma").build();
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
                        return Response.status(404).entity("Cuenta origen no encontrada").build();
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return Response.status(400).entity("La cuenta origen no está activa").build();
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
                        return Response.status(404).entity("Cuenta destino no encontrada").build();
                    }
                    String estado = rs.getString("vch_cuenestado");
                    if (!"ACTIVO".equals(estado)) {
                        cn.rollback();
                        return Response.status(400).entity("La cuenta destino no está activa").build();
                    }
                    contadorDestino = rs.getInt("int_cuencontmov");
                }
            }

            // 3. Validar saldo suficiente en ORIGEN
            if (saldoOrigen < req.getImporte()) {
                cn.rollback();
                return Response.status(400).entity("Saldo insuficiente en la cuenta origen").build();
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
                pstm.setBigDecimal(4, java.math.BigDecimal.valueOf(req.getImporte()));
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
            return Response.ok("Transferencia realizada exitosamente").build();

        } catch (Exception e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return Response.status(500).entity("Error al realizar la transferencia: " + e.getMessage()).build();
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}