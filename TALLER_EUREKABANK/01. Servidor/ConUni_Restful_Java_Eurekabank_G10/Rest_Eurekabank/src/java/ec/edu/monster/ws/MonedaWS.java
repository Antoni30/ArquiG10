package ec.edu.monster.ws;

import ec.edu.monster.modelo.Moneda;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/monedas")
@Produces(MediaType.APPLICATION_JSON)
public class MonedaWS {

    @GET
    public Response listar() {
        List<Moneda> lista = new ArrayList<>();
        String sql = "SELECT chr_monecodigo, vch_monedescripcion FROM Moneda";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Moneda m = new Moneda(
                    rs.getString("chr_monecodigo"),
                    rs.getString("vch_monedescripcion")
                );
                lista.add(m);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar monedas").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
        String sql = "SELECT chr_monecodigo, vch_monedescripcion FROM Moneda WHERE chr_monecodigo = ?";
        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Moneda m = new Moneda(
                        rs.getString("chr_monecodigo"),
                        rs.getString("vch_monedescripcion")
                    );
                    return Response.ok(m).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("Moneda no encontrada").build();
    }
}