package ec.edu.moster.ws;

import ec.edu.moster.servicios.ConversionUnidades;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Rodrigo Toapanta, Angel Castillo, Ricardo Lazo
 */
@WebService(serviceName = "WSConversionUnidades")
public class WSConversionUnidades {
    private final ConversionUnidades cu = new ConversionUnidades();

    // --------------------
    // LONGITUD
    // --------------------

    @WebMethod(operationName = "centimetrosAMetros")
    public double centimetrosAMetros(@WebParam(name = "centimetros") double centimetros) {
        return cu.centimetrosAMetros(centimetros);
    }

    @WebMethod(operationName = "metrosACentimetros")
    public double metrosACentimetros(@WebParam(name = "metros") double metros) {
        return cu.metrosACentimetros(metros);
    }

    @WebMethod(operationName = "metrosAKilometros")
    public double metrosAKilometros(@WebParam(name = "metros") double metros) {
        return cu.metrosAKilometros(metros);
    }

    // --------------------
    // MASA
    // --------------------

    @WebMethod(operationName = "toneladaALibra")
    public double toneladaALibra(@WebParam(name = "toneladas") double toneladas) {
        return cu.toneladaALibra(toneladas);
    }

    @WebMethod(operationName = "kilogramoALibra")
    public double kilogramoALibra(@WebParam(name = "kilogramos") double kilogramos) {
        return cu.kilogramoALibra(kilogramos);
    }

    @WebMethod(operationName = "gramoAKilogramo")
    public double gramoAKilogramo(@WebParam(name = "gramos") double gramos) {
        return cu.gramoAKilogramo(gramos);
    }

    // --------------------
    // TEMPERATURA
    // --------------------

    @WebMethod(operationName = "celsiusAFahrenheit")
    public double celsiusAFahrenheit(@WebParam(name = "celsius") double celsius) {
        return cu.celsiusAFahrenheit(celsius);
    }

    @WebMethod(operationName = "fahrenheitACelsius")
    public double fahrenheitACelsius(@WebParam(name = "fahrenheit") double fahrenheit) {
        return cu.fahrenheitACelsius(fahrenheit);
    }

    @WebMethod(operationName = "celsiusAKelvin")
    public double celsiusAKelvin(@WebParam(name = "celsius") double celsius) {
        return cu.celsiusAKelvin(celsius);
    }
}
