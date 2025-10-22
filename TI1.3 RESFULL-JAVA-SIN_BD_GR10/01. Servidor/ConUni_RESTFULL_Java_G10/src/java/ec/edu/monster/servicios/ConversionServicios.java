package ec.edu.monster.servicios;

import ec.edu.monster.utils.ConversionUnidades;
import ec.edu.mosnter.modelos.Entrada;
import ec.edu.mosnter.modelos.Resultado;

/**
 *
 * @author rodri
 */
public class ConversionServicios {

    private final ConversionUnidades cu = new ConversionUnidades();
    private final Resultado res = new Resultado();

    public Resultado convertir(Entrada entrada) {
        double valor = entrada.getValor();
        String tipo = entrada.getTipo();

        switch (tipo) {
            case "centimetroAmetros":
                res.setResultado(cu.centimetrosAMetros(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "metrosAcentimetros":
                res.setResultado(cu.metrosACentimetros(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "metrosAkilometros":
                res.setResultado(cu.metrosAKilometros(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "kilogramosAlibras":
                res.setResultado(cu.kilogramoALibra(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "gramosAkilogramos":
                  res.setResultado( cu.gramoAKilogramo(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "toneladasAlibras":
                  res.setResultado(cu.toneladaALibra(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "celciusAfahrenheit":
                res.setResultado(cu.celsiusAFahrenheit(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            case "fahrenheitAcelsius":
                  res.setResultado(cu.fahrenheitACelsius(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
                
            case "celsiusAkelvin":
                res.setResultado(cu.celsiusAKelvin(valor));
                if (res.getResultado() == -1) {
                    res.setMensaje("Error no permite negativos");
                    return res;
                }
                res.setMensaje("Exito");
                return res;
            default:
                 res.setResultado(-1);
                 res.setMensaje("Error no existe esa tipo de conversión");
                return res;
        }
    }

}
