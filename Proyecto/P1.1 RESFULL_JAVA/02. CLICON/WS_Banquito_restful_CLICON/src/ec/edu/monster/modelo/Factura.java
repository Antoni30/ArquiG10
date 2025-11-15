/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.util.List;

/**
 *
 * @author rodri
 */
public class Factura {
public static void generarFactura(Cliente cliente, List<Electrodomestico> carrito, boolean credito) {
    // Anchos de columna (ajusta si quieres más o menos espacio)
    final int nameWidth = 25;
    final int priceWidth = 10;

    String headerFmt = "%-" + nameWidth + "s %" + priceWidth + "s%n";
    String rowFmt = "%-" + nameWidth + "s %" + priceWidth + ".2f%n";
    String totalFmt = "%-" + nameWidth + "s %" + priceWidth + ".2f%n";

    int sepLen = nameWidth + 1 + priceWidth;
    String separator = new String(new char[sepLen]).replace('\0', '-');

    System.out.println("===== FACTURA =====");
    System.out.printf("Cliente: %s%n", cliente.getNombre());
    System.out.printf("Cédula: %s%n", cliente.getCedula());
    System.out.println(separator);

    // Header de la tabla
    System.out.printf(headerFmt, "Producto", "Precio");
    System.out.println(separator);

    // Filas de productos
    carrito.forEach(item -> {
        String prodName = item.getNombre();
        // Si el nombre es muy largo, lo truncamos para que no rompa el formato:
        if (prodName.length() > nameWidth) {
            prodName = prodName.substring(0, nameWidth - 3) + "...";
        }
        System.out.printf(rowFmt, prodName, item.getPrecio());
    });

    System.out.println(separator);

    double totalCompra = carrito.stream()
            .mapToDouble(Electrodomestico::getPrecio)
            .sum();

    if (credito) {
        System.out.printf(totalFmt, "TOTAL A PAGAR:", totalCompra);
    } else {
        double descuento = totalCompra * 0.33; // 33% de descuento
        System.out.printf(totalFmt, "TOTAL:", totalCompra);
        System.out.printf(totalFmt, "DESCUENTO:", descuento);
        System.out.printf(totalFmt, "TOTAL A PAGAR:", totalCompra - descuento);
    }

    System.out.println(new String(new char[sepLen]).replace('\0', '='));
}

}
