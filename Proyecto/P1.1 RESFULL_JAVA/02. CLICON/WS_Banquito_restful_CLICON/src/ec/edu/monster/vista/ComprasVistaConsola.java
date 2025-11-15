/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.controlador.ComprarController;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Electrodomestico;
import ec.edu.monster.modelo.Factura;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ckan1
 */
public class ComprasVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ElectrodomesticoController controller = new ElectrodomesticoController();
    private final ComprarController comprarController = new ComprarController();
    private final List<Electrodomestico> carrito = new ArrayList<>();

    public void mostrarMenu() {
        System.out.println("\n=== Comprar electrodomesticos ===");
        try {
            List<Electrodomestico> electrodomesticosDisponibles = controller.obtenerElectrodomesticos();
            if (electrodomesticosDisponibles.isEmpty()) {
                System.out.println("No hay electrodomesticos disponibles para la compra.");
                return;
            }
            
            while (true) {
                System.out.println("\nelectrodomesticos disponibles:");
                for (int i = 0; i < electrodomesticosDisponibles.size(); i++) {
                    Electrodomestico celular = electrodomesticosDisponibles.get(i);
                    System.out.printf("%d. %s ($%.2f)\n", i + 1, celular.getNombre(), celular.getPrecio());
                }
                System.out.println("0. Finalizar selección");
                
                System.out.print("Seleccione el número del electrodomestico a comprar o 0 para continuar: ");
                int seleccion = Integer.parseInt(scanner.nextLine()) - 1;
                
                if (seleccion == -1) {
                    break;
                }
                
                if (seleccion < 0 || seleccion >= electrodomesticosDisponibles.size()) {
                    System.out.println("Selección inválida.");
                    continue;
                }
                
                carrito.add(electrodomesticosDisponibles.get(seleccion));
                System.out.println("Electrodomestico agregado al carrito.");
            }
            
            if (carrito.isEmpty()) {
                System.out.println("No se seleccionó ningún celular.");
                return;
            }
            
            double totalCompra = carrito.stream().mapToDouble(Electrodomestico::getPrecio).sum();
            
            System.out.println("Seleccione el método de pago:");
            System.out.println("1. Crédito");
            System.out.println("2. Efectivo (33% de descuento)");
            System.out.print("Opción: ");
            int metodoPago = Integer.parseInt(scanner.nextLine());
            
            if (metodoPago == 1) {
                System.out.print("Ingrese su número de cédula: ");
                String cedula = scanner.nextLine().trim();

                if (cedula.isEmpty()) {
                    System.out.println("La cédula no puede estar vacía.");
                    return;
                }

                if (!comprarController.esSujetoCredito(cedula)) {
                    System.out.println("El cliente no es sujeto de crédito.");
                    System.err.println("Razon: "+ comprarController.razon(cedula));
                    return;
                }
                
                double montoMaximo = comprarController.calcularMontoMaximo(cedula);
                System.out.println("Total: "+ totalCompra);
                if (totalCompra > montoMaximo) {
                    System.out.println("El total de la compra excede el monto máximo permitido: $" + montoMaximo);
                    return;
                }
                else{
                    System.out.println("Aprobado Credito ✅");
                }
                System.out.println("Seleccione el plazo de credito (>=3 meses o =<24 meses )");
                System.out.println("(Recuerde: La tasa de interes es de 16% anual) ");
                System.out.print("N°  plazo: ");
                int credito = Integer.parseInt(scanner.nextLine());
              
                boolean compraExitosa = comprarController.comprarCredito(cedula, totalCompra, credito);
                if (compraExitosa) {
                    System.out.println("Compra a crédito realizada con éxito.");
                     Cliente c = comprarController.datosCliente(cedula);
                      Factura.generarFactura(c, carrito, true);
                       carrito.clear();
                } else {
                    System.out.println("Error al procesar la compra a crédito.");
                }
            } else if (metodoPago == 2) {
                 Cliente c = new Cliente("Consumidor Final","0000000000");
                Factura.generarFactura(c, carrito, false);
                 carrito.clear();
            } else {
                System.out.println("Método de pago inválido.");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar la compra: " + e.getMessage());
        }
    }
}
