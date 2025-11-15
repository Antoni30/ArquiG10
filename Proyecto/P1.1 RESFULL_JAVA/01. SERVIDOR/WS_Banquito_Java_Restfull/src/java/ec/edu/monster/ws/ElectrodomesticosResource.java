/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.ws;

import ec.edu.monster.modelo.Electrodomesticos;
import ec.edu.monster.servicio.ElectrodomesticoService;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author sdiegx
 */
@Path("Electrodomesticos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ElectrodomesticosResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ElectrodomesticosResource
     */
    //    @Inject
    private ElectrodomesticoService electrodomesticosService;
    public ElectrodomesticosResource() {
        this.electrodomesticosService = new ElectrodomesticoService();
    }



    @GET
    public Response getAllElectrodomesticos() {
        try {
            List<Electrodomesticos> electrodomesticos = electrodomesticosService.getAll();
            return Response.ok(electrodomesticos).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los electrodomesticos.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCelularById(@PathParam("id") int id) {
        try {
            Electrodomesticos celular = electrodomesticosService.getById(id);
            if (celular == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Celular no encontrado.").build();
            }
            return Response.ok(celular).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el celular.").build();
        }
    }

    @POST
    public Response createCelular(Electrodomesticos celular) {
        try {
            electrodomesticosService.create(celular);
            return Response.status(Response.Status.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el celular.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCelular(@PathParam("id") int id, Electrodomesticos celular) {
        try {
            boolean updated = electrodomesticosService.update(id, celular);
            if (!updated) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Celular no encontrado para actualizar.").build();
            }
            return Response.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el celular.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCelular(@PathParam("id") int id) {
        try {
            boolean deleted = electrodomesticosService.delete(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Celular no encontrado para eliminar.").build();
            }
            return Response.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar el celular.").build();
        }
    }
}
