/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.Electrodomestico;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ckan1
 */
public class ElectrodomesticoVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ElectrodomesticoController controller = new ElectrodomesticoController();

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== Gestión de electrodomesticos ===");
            System.out.println("1. Ver lista de electrodomesticos");
            System.out.println("2. Agregar un nuevo Electrodomestico");
            System.out.println("3. Editar un Electrodomestico");
            System.out.println("4. Eliminar un Electrodomestico");
            System.out.println("5. Volver al menú principal");
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
                    listarelectrodomesticos();
                    break;
                case 2:
                    agregarElectrodomestico();
                    break;
                case 3:
                    editarElectrodomestico();
                    break;
                case 4:
                    eliminarElectrodomestico();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }
    }

    private void listarelectrodomesticos() {
        try {
            List<Electrodomestico> electrodomesticos = controller.obtenerElectrodomesticos();
            if (electrodomesticos.isEmpty()) {
                System.out.println("No hay electrodomesticos registrados.");
                return;
            }
            System.out.println("\n=== Lista de electrodomesticos ===");
            for (Electrodomestico Electrodomestico : electrodomesticos) {
                System.out.printf("ID: %d | %s  | Precio: $%.2f\n",
                        Electrodomestico.getId(), Electrodomestico.getNombre(), Electrodomestico.getPrecio());
            }
        } catch (IOException e) {
            System.out.println("Error al obtener los electrodomesticos: " + e.getMessage());
        }
    }

    private void agregarElectrodomestico() {
        System.out.println("\n=== Agregar Electrodomestico ===");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Precio: ");
        double precio;

        try {
            precio = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("El precio debe ser un número válido.");
            return;
        }

        System.out.print("Foto URL: ");
        String fotoUrl = scanner.nextLine();

        Electrodomestico nuevoElectrodomestico = new Electrodomestico();
        nuevoElectrodomestico.setNombre(nombre);
        nuevoElectrodomestico.setPrecio(precio);
        nuevoElectrodomestico.setFotoUrl(fotoUrl);

        try {
            if (controller.crearElectrodomestico(nuevoElectrodomestico)) {
                System.out.println("Electrodomestico agregado exitosamente.");
            } else {
                System.out.println("Error al agregar el Electrodomestico.");
            }
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    private void editarElectrodomestico() {
        System.out.println("\n=== Editar Electrodomestico ===");
        System.out.print("Ingrese el ID del Electrodomestico a editar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        try {
            List<Electrodomestico> electrodomesticos = controller.obtenerElectrodomesticos();
            Electrodomestico Electrodomestico = electrodomesticos.stream().filter(c -> c.getId() == id).findFirst().orElse(null);

            if (Electrodomestico == null) {
                System.out.println("Electrodomestico no encontrado.");
                return;
            }

            System.out.print("Nuevo nombre (actual: " + Electrodomestico.getNombre() + "): ");
            String nuevoNombre = scanner.nextLine();
            System.out.print("Nuevo precio (actual: $" + Electrodomestico.getPrecio() + "): ");
            double nuevoPrecio;

            try {
                nuevoPrecio = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("El precio debe ser un número válido.");
                return;
            }

            System.out.print("Nueva Foto URL (actual: " + Electrodomestico.getFotoUrl() + "): ");
            String nuevaFotoUrl = scanner.nextLine();

            Electrodomestico.setNombre(nuevoNombre);
            Electrodomestico.setPrecio(nuevoPrecio);
            Electrodomestico.setFotoUrl(nuevaFotoUrl);

            if (controller.editarElectrodomestico(Electrodomestico)) {
                System.out.println("Electrodomestico actualizado correctamente.");
            } else {
                System.out.println("Error al actualizar el Electrodomestico.");
            }
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    private void eliminarElectrodomestico() {
        System.out.println("\n=== Eliminar Electrodomestico ===");
        System.out.print("Ingrese el ID del Electrodomestico a eliminar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        if (controller.eliminarElectrodomestico(id)) {
            System.out.println("Electrodomestico eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar el Electrodomestico.");
        }
    }
}
