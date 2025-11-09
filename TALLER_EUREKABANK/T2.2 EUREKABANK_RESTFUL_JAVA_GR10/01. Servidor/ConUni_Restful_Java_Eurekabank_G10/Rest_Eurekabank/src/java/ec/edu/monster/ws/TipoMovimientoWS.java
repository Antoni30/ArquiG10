package ec.edu.monster.ws;

import ec.edu.monster.modelo.TipoMovimiento;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/tiposmovimiento")
@Produces(MediaType.APPLICATION_JSON)
public class TipoMovimientoWS {

    @GET
    public Response listar() {
        List<TipoMovimiento> lista = new ArrayList<>();
        String sql = "SELECT chr_tipocodigo, vch_tipodescripcion, vch_tipoaccion, vch_tipoestado " +
                     "FROM TipoMovimiento";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                TipoMovimiento t = new TipoMovimiento(
                    rs.getString("chr_tipocodigo"),
                    rs.getString("vch_tipodescripcion"),
                    rs.getString("vch_tipoaccion"),
                    rs.getString("vch_tipoestado")
                );
                lista.add(t);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar tipos de movimiento").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
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
                    return Response.ok(t).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("Tipo de movimiento no encontrado").build();
    }
}