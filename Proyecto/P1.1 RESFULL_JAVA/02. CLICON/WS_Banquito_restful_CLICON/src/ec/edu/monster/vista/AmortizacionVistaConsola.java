/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ConsultarAmortizacionController;
import ec.edu.monster.modelo.Amortizacion;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ckan1
 */
public class AmortizacionVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsultarAmortizacionController controller = new ConsultarAmortizacionController();

    public void mostrarMenu() {
        System.out.println("\n=== Consultar Tabla de Amortización ===");
        System.out.print("Ingrese su número de cédula: ");
        String cedula = scanner.nextLine().trim();
        
        if (cedula.isEmpty()) {
            System.out.println("La cédula no puede estar vacía.");
            return;
        }
        
        try {
            List<Amortizacion> tabla = controller.generarTablaAmortizacion(cedula);
            if (tabla.isEmpty()) {
                System.out.println("No se encontró información para la cédula proporcionada.");
                return;
            }
            
            System.out.println("\n=== Tabla de Amortización ===");
            System.out.printf("%-10s %-15s %-15s %-15s %-15s%n", "Cuota", "Valor Cuota", "Interés", "Capital Pagado", "Saldo");
            System.out.println("--------------------------------------------------------------------------------");
            for (Amortizacion amortizacion : tabla) {
                System.out.printf("%-10d %-15.2f %-15.2f %-15.2f %-15.2f%n", 
                    amortizacion.getNumeroCuota(), 
                    amortizacion.getValorCuota(), 
                    amortizacion.getInteres(), 
                    amortizacion.getCapitalPagado(), 
                    amortizacion.getSaldo());
            }
        } catch (Exception e) {
            System.out.println("Error al consultar la tabla de amortización: " + e.getMessage());
        }
    }
}
