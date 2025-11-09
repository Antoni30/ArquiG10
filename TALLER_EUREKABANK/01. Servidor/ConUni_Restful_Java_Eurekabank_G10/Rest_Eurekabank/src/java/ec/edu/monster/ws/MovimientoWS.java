package ec.edu.monster.ws;

import ec.edu.monster.modelo.Movimiento;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/movimientos")
@Produces(MediaType.APPLICATION_JSON)
public class MovimientoWS {

    @GET
    public Response listar() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, " +
                     "chr_tipocodigo, dec_moviimporte, chr_cuenreferencia FROM Movimiento";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
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
                lista.add(m);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar movimientos").build();
        }
    }

    @GET
    @Path("/cuenta/{cuenta}")
    public Response porCuenta(@PathParam("cuenta") String cuenta) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, " +
                     "chr_tipocodigo, dec_moviimporte, chr_cuenreferencia FROM Movimiento " +
                     "WHERE chr_cuencodigo = ?";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, cuenta);
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
                return Response.ok(lista).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("No se encontraron movimientos").build();
    }
}