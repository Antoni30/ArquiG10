/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clicon_java_soap;

import ec.edu.mosnter.ws.Cuenta;
import ec.edu.mosnter.ws.Empleado;
import ec.edu.mosnter.ws.Movimiento;
import ec.edu.mosnter.ws.MovimientoRequest;
import ec.edu.mosnter.ws.TipoMovimiento;
import ec.edu.mosnter.ws.TransferenciaRequest;
import ec.edu.mosnter.ws.WSEurekabank_Service;
import ec.edu.mosnter.ws.WSEurekabank;
import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author rodri
 */
public class CLICON_JAVA_SOAP {

    private static final String USUARIO_VALIDO = "MONSTER";
    private static final String CONTRASENA_VALIDA = "MONSTER9";
    private static final WSEurekabank_Service service = new WSEurekabank_Service();
    private static final WSEurekabank port = service.getWSEurekabankPort();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Advertencia: No se pudo establecer la codificación UTF-8 para la salida.");
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 🔐 LOGIN EUREKABANK ===");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();

        String pass = leerContraseñaOculta(scanner);
        limpiarPantalla();

        if (!USUARIO_VALIDO.equals(user) || !CONTRASENA_VALIDA.equals(pass)) {
            System.out.println("❌ Credenciales incorrectas. Acceso denegado.");
            return;
        }

        System.out.println("✅ ¡Inicio de sesión exitoso!");
        System.out.println("✅ ¡Inicio de sesión exitoso!");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = 0;
            try {
                System.out.print("Elige una opción: ");
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opción inválida. Intenta de nuevo.");
                pausar(scanner);
                limpiarPantalla();
                continue;
            }

            switch (opcion) {
                case 1:
                    limpiarPantalla();
                    consultarMovimientos(scanner);
                    limpiarPantalla();
                    break;
                case 2:
                    limpiarPantalla();
                    realizarDeposito(scanner);
                    limpiarPantalla();
                    break;
                case 3:
                    limpiarPantalla();
                     realizarTransferencia(scanner);
                    limpiarPantalla();
                    break;
                case 4:
                    limpiarPantalla();
                    realizarRetiro(scanner);
                    limpiarPantalla();
                    break;
                case 5:
                    salir = true;
                    limpiarPantalla();
                    System.out.println("👋 ¡Gracias por usar Eurekabank!");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Intenta de nuevo.");
                    pausar(scanner);
                    limpiarPantalla();
            }
        }
        scanner.close();
    }

    private static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // Comando para limpiar la consola en Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Comandos ANSI para limpiar la consola en sistemas Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si la limpieza falla, imprime muchas líneas nuevas
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static void pausar(Scanner scanner) {
        System.out.print("\nPresiona Enter para continuar...");
        try {
            // Consumir la línea pendiente o esperar una nueva
            scanner.nextLine();
        } catch (Exception e) {
        }
    }

    private static void mostrarMenu() {
        System.out.println(String.join("", Collections.nCopies(50, "=")));
        System.out.println("             🏦 MENÚ EUREKABANK");
        System.out.println(String.join("", Collections.nCopies(50, "=")));
        System.out.println("1. Consultar Movimientos");
        System.out.println("2. Realizar Depósito");
        System.out.println("3. Transferencia");
        System.out.println("4. Realizar Retiro");
        System.out.println("5. Salir");
        System.out.println(String.join("", Collections.nCopies(50, "=")));
    }

    private static String leerContraseñaOculta(Scanner scanner) {
        Console console = System.console();

        if (console != null) {
            // Ocultación REAL de caracteres en una terminal externa
            char[] passwordArray = console.readPassword("Contraseña: ");
            return new String(passwordArray);
        } else {
            // Fallback para IDEs (NetBeans, IntelliJ, Eclipse)
            System.out.print("Contraseña: ");
            System.out.println("(Escribe tu contraseña y presiona Enter. Se borrará la pantalla inmediatamente.)");

            String password = scanner.nextLine();
            return password;
        }
    }

    private static void consultarMovimientos(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.");
            pausar(scanner);
            return;
        }
         if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.");
            pausar(scanner);
            return;
        }
        List<Movimiento> result = new ArrayList<Movimiento>();
        result = port.obtenerMovimientos(cuenta);

        if (result.isEmpty()) {
            System.out.println("ℹ️ La cuenta no tiene movimientos o hubo un error.");
            pausar(scanner);
            return;
        }
        System.out.println("\nMovimientos de la cuenta " + cuenta + " (Orden Ascendente):");
        // Ajuste el tamaño de la línea de separación
        System.out.println(String.join("", Collections.nCopies(115, "-")));
        // Ajuste el encabezado para incluir las nuevas columnas
        System.out.printf("%-7s %-10s %-20s %-5s %-25s %-10s %-10s%n",
                "NroMov", "Cuenta", "Fecha", "Cód.", "Tipo", "Acción", "Importe");
        System.out.println(String.join("", Collections.nCopies(115, "-")));

        for (Movimiento c : result) {
            System.out.printf("%-7s %-10s %-20s %-5s %-25s %-10s %-10s%n",
                    c.getNumero(),
                    c.getCuentaCodigo(),
                    c.getFecha(),
                    c.getTipoCodigo(),
                    obtenerTipoDescripcion(c.getTipoCodigo()),
                    obtenerAccionPorTipo(c.getTipoCodigo()),
                    c.getImporte());
        }

        System.out.println(String.join("", Collections.nCopies(115, "-")));
        pausar(scanner);

    }
    
     private static void realizarDeposito(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();
        
        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.");
                pausar(scanner);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();
        
        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.");
            pausar(scanner);
            return;
        }
        String res;
         MovimientoRequest mv = new MovimientoRequest();
         mv.setCuentaCodigo(cuenta);
         mv.setEmpleadoCodigo(empleado);
         mv.setImporte(importe);
         port.hacerDeposito(mv);
         System.out.println("✅ Depósito realizado con éxito.");
        pausar(scanner);
    }
    
     private static void realizarRetiro(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();
        
        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.");
                pausar(scanner);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();
        
        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.");
            pausar(scanner);
            return;
        }
       MovimientoRequest mv = new MovimientoRequest();
       mv.setCuentaCodigo(cuenta);
       mv.setEmpleadoCodigo(empleado);
       mv.setImporte(importe); 
        port.hacerRetiro(mv);
        System.out.println("✅ Retiro realizado con éxito.");
        pausar(scanner);
    }
     
     private static void realizarTransferencia(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta ORIGEN: ");
        String cuentaOrigen = scanner.nextLine().trim();
        
        if (cuentaOrigen.isEmpty()) {
            System.out.println("❌ El número de cuenta origen no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeCuenta(cuentaOrigen)) {
            System.out.println("❌ La cuenta origen no existe.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el número de cuenta DESTINO: ");
        String cuentaDestino = scanner.nextLine().trim();
        
        if (cuentaDestino.isEmpty()) {
            System.out.println("❌ El número de cuenta destino no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeCuenta(cuentaDestino)) {
            System.out.println("❌ La cuenta destino no existe.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.");
                pausar(scanner);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.");
            pausar(scanner);
            return;
        }
        
        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();
        
        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.");
            pausar(scanner);
            return;
        }
        
        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.");
            pausar(scanner);
            return;
        }
        
       TransferenciaRequest tr = new TransferenciaRequest();
       tr.setCuentaDestino(cuentaDestino);
       tr.setCuentaOrigen(cuentaOrigen);
       tr.setImporte(importe);
       tr.setEmpleadoCodigo(empleado);
        port.hacerTransferencia(tr);

        System.out.println("✅ Transferencia realizada con éxito.");
 
        pausar(scanner);
    }
     
    private static String obtenerTipoDescripcion(String tipoCodigo) {
        TipoMovimiento t = new TipoMovimiento();
        t = port.tipoMoviento(tipoCodigo);
        return t.getDescripcion();
    }

    private static String obtenerAccionPorTipo(String tipoCodigo) {
        TipoMovimiento t = new TipoMovimiento();
        t = port.tipoMoviento(tipoCodigo);
        return t.getAccion();
    }

     private static boolean existeCuenta(String cuenta) {
          Cuenta c = new Cuenta();
          c = port.buscarPorId(cuenta);
        return c != null;
        }
      
     private static boolean existeEmpleado(String empleado) {
        Empleado e = new Empleado();
        e = port.buscarPorIdEmpleado(empleado);
        return e!=null;
    }
      
}
