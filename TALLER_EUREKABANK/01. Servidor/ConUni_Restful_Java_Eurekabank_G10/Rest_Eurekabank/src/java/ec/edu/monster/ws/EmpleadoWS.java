package ec.edu.monster.ws;

import ec.edu.monster.modelo.Empleado;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/empleados")
@Produces(MediaType.APPLICATION_JSON)
public class EmpleadoWS {

    @GET
    public Response listar() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT chr_emplcodigo, vch_emplpaterno, vch_emplmaterno, vch_emplnombre, " +
                     "vch_emplciudad, vch_empldireccion FROM Empleado";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado(
                    rs.getString("chr_emplcodigo"),
                    rs.getString("vch_emplpaterno"),
                    rs.getString("vch_emplmaterno"),
                    rs.getString("vch_emplnombre"),
                    rs.getString("vch_emplciudad"),
                    rs.getString("vch_empldireccion")
                );
                lista.add(e);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar empleados").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
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
                    return Response.ok(e).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("Empleado no encontrado").build();
    }
}