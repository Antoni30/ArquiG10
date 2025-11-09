package ec.edu.monster.servicio;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Clase de configuración para activar los servicios REST.
 * Todos los recursos (endpoints) estarán disponibles bajo la ruta base: /api
 */
@ApplicationPath("api")
public class EurekaService extends Application {
    // Esta clase no necesita métodos.
    // La anotación @ApplicationPath("api") es suficiente para que GlassFish
    // detecte y active todos los recursos REST del proyecto (clases con @Path).
}