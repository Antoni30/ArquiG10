package ec.edu.monster.ws;

import ec.edu.monster.modelo.Cliente;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
public class ClienteWS {

    @GET
    public Response listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT chr_cliecodigo, vch_cliepaterno, vch_cliematerno, vch_clienombre, " +
                     "chr_cliedni, vch_clieciudad, vch_cliedireccion, vch_clietelefono, vch_clieemail " +
                     "FROM Cliente";

        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
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
                lista.add(c);
            }
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al listar clientes").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
        String sql = "SELECT chr_cliecodigo, vch_cliepaterno, vch_cliematerno, vch_clienombre, " +
                     "chr_cliedni, vch_clieciudad, vch_cliedireccion, vch_clietelefono, vch_clieemail " +
                     "FROM Cliente WHERE chr_cliecodigo = ?";
        try (Connection cn = ec.edu.monster.db.AccesoDB.getConnection();
             PreparedStatement pstm = cn.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
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
                    return Response.ok(c).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).entity("Cliente no encontrado").build();
    }
}