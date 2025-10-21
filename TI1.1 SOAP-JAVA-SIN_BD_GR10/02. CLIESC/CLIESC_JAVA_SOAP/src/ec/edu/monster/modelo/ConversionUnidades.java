
package ec.edu.monster.modelo;

import ec.edu.monster.servicios.WSConversionUnidades_Service;
import ec.edu.monster.servicios.WSConversionUnidades;

/**
 *
 * @author rodri
 */
public class ConversionUnidades {
    
      private final WSConversionUnidades_Service service = new WSConversionUnidades_Service();
      private final WSConversionUnidades port = service.getWSConversionUnidadesPort();
      
        public double centimetrosAMetros(double centimetros) {
                return port.centimetrosAMetros(centimetros);
        }

    public double metrosACentimetros(double metros) {
            return port.metrosACentimetros(metros);
    }


    public double metrosAKilometros(double metros) {
            return port.metrosAKilometros(metros);
    }

    public double toneladaALibra(double tonelada) {
        return port.toneladaALibra(tonelada);
    }


    public double kilogramoALibra(double kilogramo) {
        return port.kilogramoALibra(kilogramo);
    }


    public double gramoAKilogramo(double gramos) {
        return port.gramoAKilogramo(gramos);
    }

    public double celsiusAFahrenheit(double celsius) {
        return port.celsiusAFahrenheit(celsius);
    }

    public double fahrenheitACelsius(double fahrenheit) {
        return port.fahrenheitACelsius(fahrenheit);
    }

    public double celsiusAKelvin(double celsius) {
        return port.celsiusAKelvin(celsius);
    }

}
