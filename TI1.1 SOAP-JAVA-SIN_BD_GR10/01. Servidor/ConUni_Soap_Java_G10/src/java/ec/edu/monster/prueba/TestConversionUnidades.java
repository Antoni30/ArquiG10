package ec.edu.monster.prueba;

import ec.edu.moster.servicios.ConversionUnidades;

/**
 *
 * @author rodri
 */
public class TestConversionUnidades {
    public static void main(String[] args) {
        ConversionUnidades cu = new ConversionUnidades();
        double num=10;
        double res;
        res = cu.centimetrosAMetros(num);
        System.out.println("La conversion es "+res);
    }
}
