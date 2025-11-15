/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.ws;

import ec.edu.monster.modelo.Amortizacion;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.CompraRequest;
import ec.edu.monster.modelo.CreditoRequest;
import ec.edu.monster.servicio.BanquitoService;
import javax.ws.rs.core.*;
import java.util.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
/**
 * REST Web Service
 *
 * @author sdiegx
 */
@Path("Banquito")
public class BanquitoResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BanquitoResource
     */
    public BanquitoResource() {
    }

    private BanquitoService service = new BanquitoService();

    @POST
    @Path("/ProcesarCompra")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarCompra(CompraRequest request) {
        if (request == null || request.getCedula() == null || request.getCedula().isEmpty() || (Double) request.getCosto() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("mensaje", "Datos inválidos. Asegúrese de que la cédula y el costo sean correctos.")).build();
        }

        Map<String, Object> resultado = service.procesarCompra( request.getCedula(), request.getCosto());

        if (!(Boolean) resultado.get("result")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("mensaje", resultado.get("message"))).build();
        }

        return Response.ok(Collections.singletonMap("mensaje", resultado.get("message"))).build();
    }

    @GET
    @Path("/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarCliente(@PathParam("cedula") String cedula) {
        Cliente cliente = service.verificarClientePorCedula(cedula);

        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("mensaje", "Cliente no encontrado.")).build();
        }

        return Response.ok(cliente).build();
    }
    
    @GET
    @Path("/OtorgarCredito/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response esSujetoDeCredito(@PathParam("cedula") String cedula) {
        Map<String, Object> resultado = service.esSujetoDeCreditoConDetalles(cedula);
        
       
        
        if (!(Boolean) resultado.get("sujetoDeCredito")) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El cliente no es sujeto de crédito.");
            response.put("razon", resultado.get("razon"));
            return Response.ok(response).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "El cliente es sujeto de crédito.");
        response.put("razon", resultado.get("razon"));
        return Response.ok(response).build();
    }

    @GET
    @Path("/CalcularMontoMaximo/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularMontoMaximoPorCedula(@PathParam("cedula") String cedula) {
        double montoMaximo = service.calcularMontoMaximoCredito(cedula);

        if (montoMaximo <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("mensaje", "El cliente no tiene suficiente capacidad de crédito.")).build();
        }

        return Response.ok(Collections.singletonMap("montoMaximo", montoMaximo)).build();
    }
    
    @POST
    @Path("/RegistrarCredito")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarCredito(CreditoRequest request) {
        if (request == null || request.getCedula() == null || request.getCedula().isEmpty() || request.getMonto() <= 0 || request.getPlazo() < 3 || request.getPlazo() > 24) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("mensaje", "Datos inválidos. Asegúrese de que la cédula, el monto y el plazo sean correctos.")).build();
        }
        
        Map<String, Object> resultado = service.asignarCredito(request.getCedula(), request.getMonto(), request.getPlazo());

        if (!(Boolean) resultado.get("result")) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error");
            response.put("razon", resultado.get("status"));
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Éxito");
        response.put("razon", resultado.get("status"));
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/GenerarTablaAmortizacion/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarTablaAmortizacion(@PathParam("cedula") String cedula) {
        List<Amortizacion> tabla = service.generarTablaAmortizacion(cedula);
        return Response.ok(tabla).build();
    }
}
