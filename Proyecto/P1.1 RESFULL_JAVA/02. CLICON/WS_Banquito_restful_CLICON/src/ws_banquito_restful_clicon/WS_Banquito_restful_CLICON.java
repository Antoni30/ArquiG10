/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws_banquito_restful_clicon;

import ec.edu.monster.vista.AmortizacionVistaConsola;
import ec.edu.monster.vista.ElectrodomesticoVistaConsola;
import ec.edu.monster.vista.ComprasVistaConsola;
import java.util.Scanner;

/**
 *
 * @author ckan1
 */
public class WS_Banquito_restful_CLICON {

    /**
     * @param args the command line arguments
     */
    private static final Scanner scanner = new Scanner(System.in);
    private static final ElectrodomesticoVistaConsola celularesVista = new ElectrodomesticoVistaConsola();
    private static final ComprasVistaConsola comprasVista = new ComprasVistaConsola();
    private static final AmortizacionVistaConsola amortizacionVista = new AmortizacionVistaConsola();
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Menú Principal ===");
            System.out.println("1. Gestionar Electrodomesticos");
            System.out.println("2. Comprar Electrodomesticos");
            System.out.println("3. Consultar Tabla de Amortización");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    celularesVista.mostrarMenu();
                    break;
                case 2:
                    comprasVista.mostrarMenu();
                    break;
                case 3:
                    amortizacionVista.mostrarMenu();
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }
    }
    
}
