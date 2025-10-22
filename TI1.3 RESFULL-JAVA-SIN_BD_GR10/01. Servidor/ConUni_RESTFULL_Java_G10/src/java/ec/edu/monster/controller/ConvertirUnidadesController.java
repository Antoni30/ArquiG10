package ec.edu.monster.controller;


import ec.edu.monster.servicios.ConversionServicios;
import ec.edu.mosnter.modelos.Entrada;
import ec.edu.mosnter.modelos.Resultado;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


/**
 * REST Web Service
 *
 * @author rodri
 */
@Path("/servicio")
public class ConvertirUnidadesController {

    private final ConversionServicios cs = new ConversionServicios();
    
    public ConvertirUnidadesController(UriInfo context) {
        this.context = context;
    }
   
        
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CovertirUnidadesServicio
     */
    public ConvertirUnidadesController() {
    }
    
    @POST
    @Path("/conversionUnidades")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Resultado  convertir(  Entrada entrada){
       
        return cs.convertir(entrada);
    }
}
