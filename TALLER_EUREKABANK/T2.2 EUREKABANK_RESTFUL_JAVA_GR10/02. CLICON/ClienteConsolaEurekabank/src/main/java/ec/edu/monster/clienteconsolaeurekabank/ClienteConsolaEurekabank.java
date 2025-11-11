package ec.edu.monster.clienteconsolaeurekabank;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClienteConsolaEurekabank {

    // Asegúrate de que tu IDE o terminal use UTF-8 para mostrar correctamente los caracteres especiales.
    // En NetBeans, esto a menudo se soluciona añadiendo -J-Dfile.encoding=UTF-8 en las propiedades del proyecto.
    private static final String BASE_URL = "http://localhost:8080/Rest_Eurekabank/api";
    private static final String USUARIO_VALIDO = "MONSTER";
    private static final String CONTRASENA_VALIDA = "MONSTER9";

    public static void main(String[] args) {
        // Establecer la codificación de salida a UTF-8 (útil en algunos entornos de consola)
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
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
    
    private static void pausar(Scanner scanner) {
        System.out.print("\nPresiona Enter para continuar...");
        try {
            // Consumir la línea pendiente o esperar una nueva
            scanner.nextLine();
        } catch (Exception e) {}
    }
    
    private static void mostrarMenu() {
        System.out.println("=".repeat(50));
        System.out.println("             🏦 MENÚ EUREKABANK");
        System.out.println("=".repeat(50));
        System.out.println("1. Consultar Movimientos");
        System.out.println("2. Realizar Depósito");
        System.out.println("3. Transferencia");
        System.out.println("4. Realizar Retiro");
        System.out.println("5. Salir");
        System.out.println("=".repeat(50));
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
    
    // --- LÓGICA DE NEGOCIO Y API REST ---

    private static boolean existeCuenta(String cuenta) {
        String url = BASE_URL + "/cuentas/" + cuenta;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            int status = con.getResponseCode();
            return status == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean existeEmpleado(String empleado) {
        String url = BASE_URL + "/empleados/" + empleado;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            int status = con.getResponseCode();
            return status == 200;
        } catch (Exception e) {
            return false;
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
        
        String url = BASE_URL + "/cuentas/" + cuenta + "/movimientos";
        String json = enviarPeticionGET(url);
        
        if (json == null || json.trim().isEmpty() || json.contains("Cuenta no encontrada")) {
            System.out.println("ℹ️ La cuenta no tiene movimientos o hubo un error.");
            pausar(scanner);
            return;
        }
        
        String[] movimientosArray = json.split("\\},\\s*\\{");
        List<String> movimientosList = Arrays.asList(movimientosArray);
        Collections.reverse(movimientosList);
        
        System.out.println("\nMovimientos de la cuenta " + cuenta + " (Orden Ascendente):");
        // Ajuste el tamaño de la línea de separación
        System.out.println("-".repeat(115));
        // Ajuste el encabezado para incluir las nuevas columnas
        System.out.printf("%-7s %-10s %-20s %-5s %-25s %-10s %-10s%n", 
                "NroMov", "Cuenta", "Fecha", "Cód.", "Tipo", "Acción", "Importe");
        System.out.println("-".repeat(115));
        
        for (String mov : movimientosList) {
            mov = mov.replaceFirst("^\\[?\\{", "").replaceAll("\\}\\]?$", "");
            
            String numero = extraerValor(mov, "numero");
            String cuentaCodigo = extraerValor(mov, "cuentaCodigo"); // Nuevo: Código de cuenta
            String fecha = extraerValor(mov, "fecha");
            String tipoCodigo = extraerValor(mov, "tipoCodigo");
            String importeStr = extraerValor(mov, "importe");
            
            String tipoDescripcion = obtenerTipoDescripcion(tipoCodigo);
            String accion = obtenerAccionPorTipo(tipoCodigo);
            String importeFmt = formatearImporte(importeStr);
            
            // Ajuste el formato de impresión
            System.out.printf("%-7s %-10s %-20s %-5s %-25s %-10s %-10s%n",
                    numero.isEmpty() ? "?" : numero,
                    cuentaCodigo.isEmpty() ? "?" : cuentaCodigo,
                    fecha.isEmpty() ? "?" : fecha,
                    tipoCodigo.isEmpty() ? "?" : tipoCodigo,
                    tipoDescripcion.isEmpty() ? tipoCodigo : tipoDescripcion,
                    accion.isEmpty() ? "?" : accion,
                    importeFmt);
        }
        System.out.println("-".repeat(115));
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
        
        String json = "{" 
            + "\"cuentaCodigo\":\"" + cuenta + "\"," 
            + "\"importe\":" + importe + "," 
            + "\"empleadoCodigo\":\"" + empleado + "\"" 
            + "}";
        
        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/deposito", json);
        
        if (respuesta == null || respuesta.contains("Error de conexión")) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Depósito realizado con éxito.");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
        }
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
        
        String json = "{" 
            + "\"cuentaCodigo\":\"" + cuenta + "\"," 
            + "\"importe\":" + importe + "," 
            + "\"empleadoCodigo\":\"" + empleado + "\"" 
            + "}";
        
        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/retiro", json);
        
        if (respuesta == null || respuesta.contains("Error de conexión")) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Retiro realizado con éxito.");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
        }
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
        
        String json = "{" 
            + "\"cuentaOrigen\":\"" + cuentaOrigen + "\"," 
            + "\"cuentaDestino\":\"" + cuentaDestino + "\"," 
            + "\"importe\":" + importe + "," 
            + "\"empleadoCodigo\":\"" + empleado + "\"" 
            + "}";
        
        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/transferencia", json);
        
        if (respuesta == null || respuesta.contains("Error de conexión")) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Transferencia realizada con éxito.");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
        }
        pausar(scanner);
    }

    // --- MÉTODOS AUXILIARES Y DE COMUNICACIÓN HTTP ---

    private static String obtenerTipoDescripcion(String tipoCodigo) {
        String url = BASE_URL + "/tiposmovimiento/" + tipoCodigo;
        String json = enviarPeticionGET(url);
        if (json == null) return tipoCodigo;
        return extraerValor(json, "descripcion");
    }

    private static String obtenerAccionPorTipo(String tipoCodigo) {
        String url = BASE_URL + "/tiposmovimiento/" + tipoCodigo;
        String json = enviarPeticionGET(url);
        if (json == null) return "?";
        return extraerValor(json, "accion");
    }

    private static String formatearImporte(String valor) {
        if (valor == null || valor.isEmpty()) return "0,00";
        try {
            double num = Double.parseDouble(valor);
            return String.format("%.2f", num).replace('.', ',');
        } catch (NumberFormatException e) {
            return "0,00";
        }
    }

    private static String extraerValor(String jsonPart, String campo) {
        // Expresión regular para encontrar valores de string: "campo":"valor"
        String pattern = "\"" + campo + "\"\\s*:\\s*\"([^\"]*)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(jsonPart);
        if (m.find()) {
            return m.group(1);
        }
        // Expresión regular para encontrar valores numéricos/booleanos: "campo":valor
        pattern = "\"" + campo + "\"\\s*:\\s*([^,\\}]*)";
        p = java.util.regex.Pattern.compile(pattern);
        m = p.matcher(jsonPart);
        if (m.find()) {
            return m.group(1);
            }
        return "";
    }

    private static String enviarPeticionGET(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            if (status == 200) {
                // Leer la respuesta usando UTF-8 para manejar tildes/caracteres especiales
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    return content.toString();
                }
            } else {
                return "Error: " + status;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static String enviarPeticionPOST(String url, String json) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // Asegura que el servidor sepa que enviamos UTF-8
            con.setDoOutput(true);

            // Escribir el JSON codificado en UTF-8
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("UTF-8"); 
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                return "Operación realizada con éxito.";
            } else {
                // Leer el stream de error usando UTF-8
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    if (response.length() > 0) {
                         return "Error (" + status + "): " + response.toString();
                    }
                    return "Error del servidor. Código de estado: " + status;
                }
            }
        } catch (Exception e) {
            return "Error de conexión al servidor: " + e.getMessage();
        }
    }
}