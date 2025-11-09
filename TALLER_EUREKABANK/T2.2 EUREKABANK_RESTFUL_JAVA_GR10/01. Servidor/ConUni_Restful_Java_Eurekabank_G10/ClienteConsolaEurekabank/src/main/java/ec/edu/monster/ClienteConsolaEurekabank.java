package ec.edu.monster;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClienteConsolaEurekabank {

    private static final String BASE_URL = "http://localhost:8080/Rest_Eurekabank/api";
    private static final String USUARIO_VALIDO = "MONSTER";
    private static final String CONTRASENA_VALIDA = "MONSTER9";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // === LOGIN CON CONTRASEÑA OCULTA (CORREGIDA PARA NETBEANS) ===
        System.out.println("=== 🔐 LOGIN EUREKABANK ===");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = leerContraseñaOculta(); // Método corregido

        if (!USUARIO_VALIDO.equals(user) || !CONTRASENA_VALIDA.equals(pass)) {
            System.out.println("❌ Credenciales incorrectas. Acceso denegado.");
            return;
        }

        System.out.println("✅ ¡Inicio de sesión exitoso!\n");

        // === MENÚ PRINCIPAL ===
        boolean salir = false;
        while (!salir) {
            mostrarMenu(); // Mostrar menú limpio
            int opcion = 0;
            try {
                System.out.print("Elige una opción: ");
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opción inválida. Intenta de nuevo.\n");
                continue;
            }

            switch (opcion) {
                case 1:
                    consultarMovimientos(scanner);
                    break;
                case 2:
                    realizarDeposito(scanner);
                    break;
                case 3:
                    realizarTransferencia(scanner);
                    break;
                case 4:
                    realizarRetiro(scanner);
                    break;
                case 5:
                    salir = true;
                    System.out.println("👋 ¡Gracias por usar Eurekabank!");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Intenta de nuevo.\n");
            }
        }
        scanner.close();
    }

    // --- MÉTODO PARA MOSTRAR MENÚ LIMPIO ---
    private static void mostrarMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           🏦 MENÚ EUREKABANK");
        System.out.println("=".repeat(50));
        System.out.println("1. Consultar Movimientos");
        System.out.println("2. Realizar Depósito");
        System.out.println("3. Transferencia");
        System.out.println("4. Realizar Retiro");
        System.out.println("5. Salir");
        System.out.println("=".repeat(50));
    }

    // --- MÉTODO PARA LEER CONTRASEÑA OCULTA (COMPATIBLE CON NETBEANS IDE) ---
    private static String leerContraseñaOculta() {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Contraseña: ");
            return new String(passwordArray);
        } else {
            // Fallback para NetBeans IDE: leer caracter por caracter y mostrar *
            System.out.print("Contraseña: ");
            StringBuilder password = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int ch;
                while ((ch = reader.read()) != '\n' && ch != '\r') {
                    if (ch == '\b' || ch == 127) { // Retroceso
                        if (password.length() > 0) {
                            password.deleteCharAt(password.length() - 1);
                            System.out.print("\b \b"); // Borra el * visual
                        }
                    } else if (ch >= 32) { // Carácter imprimible
                        password.append((char) ch);
                        System.out.print("*");
                    }
                }
                System.out.println(); // Nueva línea
                return password.toString();
            } catch (IOException e) {
                System.out.println();
                return "";
            }
        }
    }

    // --- VALIDAR QUE LA CUENTA EXISTA ---
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

    // --- VALIDAR QUE EL EMPLEADO EXISTA ---
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

    // --- CONSULTAR MOVIMIENTOS (TABLA DESCENDENTE) ---
    private static void consultarMovimientos(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.\n");
            return;
        }

        if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.\n");
            return;
        }

        String url = BASE_URL + "/cuentas/" + cuenta + "/movimientos";
        String json = enviarPeticionGET(url);

        if (json == null || json.trim().isEmpty()) {
            System.out.println("ℹ️ La cuenta no tiene movimientos.\n");
            return;
        }

        // === MOSTRAR TABLA COMO EN LA IMAGEN (DESCENDENTE) ===
        System.out.println("\nMovimientos de la cuenta " + cuenta + ":");
        System.out.println("-".repeat(100));
        System.out.printf("%-7s %-20s %-25s %-10s %-10s%n",
                "NroMov", "Fecha", "Tipo", "Acción", "Importe");
        System.out.println("-".repeat(100));

        // Parseo del JSON
        String[] movimientos = json.split("\\},\\s*\\{");
        for (String mov : movimientos) {
            mov = mov.replaceFirst("^\\[?\\{", "").replaceAll("\\}\\]?$", "");

            String numero = extraerValor(mov, "numero");
            String fecha = extraerValor(mov, "fecha");
            String tipoCodigo = extraerValor(mov, "tipoCodigo");
            String importeStr = extraerValor(mov, "importe");

            // Obtener descripción del tipo y acción
            String tipoDescripcion = obtenerTipoDescripcion(tipoCodigo);
            String accion = obtenerAccionPorTipo(tipoCodigo);

            // Formatear importe como "2800,00"
            String importeFmt = formatearImporte(importeStr);

            System.out.printf("%-7s %-20s %-25s %-10s %-10s%n",
                    numero.isEmpty() ? "?" : numero,
                    fecha.isEmpty() ? "?" : fecha,
                    tipoDescripcion.isEmpty() ? tipoCodigo : tipoDescripcion,
                    accion.isEmpty() ? "?" : accion,
                    importeFmt
            );
        }
        System.out.println("-".repeat(100));

        // Limpiar visualmente (no borra, pero da espacio)
        System.out.println("\n".repeat(3));
        System.out.print("Presiona Enter para continuar...");
        try {
            System.in.read(); // Espera Enter
            System.out.println("\n".repeat(2)); // Espacio adicional
        } catch (IOException e) {
            // Ignorar
        }
    }

    // --- REALIZAR DEPÓSITO ---
    private static void realizarDeposito(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.\n");
            return;
        }

        if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.\n");
            return;
        }

        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.\n");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.\n");
            return;
        }

        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();

        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.\n");
            return;
        }

        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.\n");
            return;
        }

        String json = "{"
            + "\"cuentaCodigo\":\"" + cuenta + "\","
            + "\"importe\":" + importe + ","
            + "\"empleadoCodigo\":\"" + empleado + "\""
            + "}";

        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/deposito", json);

        if (respuesta == null) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.\n");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Depósito realizado con éxito.\n");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
            System.out.println();
        }

        // Limpiar visualmente
        System.out.println("\n".repeat(3));
        System.out.print("Presiona Enter para continuar...");
        try {
            System.in.read();
            System.out.println("\n".repeat(2));
        } catch (IOException e) {
            // Ignorar
        }
    }

    // --- REALIZAR RETIRO ---
    private static void realizarRetiro(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            System.out.println("❌ El número de cuenta no puede estar vacío.\n");
            return;
        }

        if (!existeCuenta(cuenta)) {
            System.out.println("❌ La cuenta no existe.\n");
            return;
        }

        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.\n");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.\n");
            return;
        }

        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();

        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.\n");
            return;
        }

        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.\n");
            return;
        }

        String json = "{"
            + "\"cuentaCodigo\":\"" + cuenta + "\","
            + "\"importe\":" + importe + ","
            + "\"empleadoCodigo\":\"" + empleado + "\""
            + "}";

        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/retiro", json);

        if (respuesta == null) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.\n");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Retiro realizado con éxito.\n");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
            System.out.println();
        }

        // Limpiar visualmente
        System.out.println("\n".repeat(3));
        System.out.print("Presiona Enter para continuar...");
        try {
            System.in.read();
            System.out.println("\n".repeat(2));
        } catch (IOException e) {
            // Ignorar
        }
    }

    // --- TRANSFERENCIA ---
    private static void realizarTransferencia(Scanner scanner) {
        System.out.print("Ingresa el número de cuenta ORIGEN: ");
        String cuentaOrigen = scanner.nextLine().trim();

        if (cuentaOrigen.isEmpty()) {
            System.out.println("❌ El número de cuenta origen no puede estar vacío.\n");
            return;
        }

        if (!existeCuenta(cuentaOrigen)) {
            System.out.println("❌ La cuenta origen no existe.\n");
            return;
        }

        System.out.print("Ingresa el número de cuenta DESTINO: ");
        String cuentaDestino = scanner.nextLine().trim();

        if (cuentaDestino.isEmpty()) {
            System.out.println("❌ El número de cuenta destino no puede estar vacío.\n");
            return;
        }

        if (!existeCuenta(cuentaDestino)) {
            System.out.println("❌ La cuenta destino no existe.\n");
            return;
        }

        System.out.print("Ingresa el importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine());
            if (importe <= 0) {
                System.out.println("❌ El importe debe ser mayor a 0.\n");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Importe inválido.\n");
            return;
        }

        System.out.print("Ingresa el código del empleado: ");
        String empleado = scanner.nextLine().trim();

        if (empleado.isEmpty()) {
            System.out.println("❌ El código del empleado no puede estar vacío.\n");
            return;
        }

        if (!existeEmpleado(empleado)) {
            System.out.println("❌ El empleado no existe.\n");
            return;
        }

        String json = "{"
            + "\"cuentaOrigen\":\"" + cuentaOrigen + "\","
            + "\"cuentaDestino\":\"" + cuentaDestino + "\","
            + "\"importe\":" + importe + ","
            + "\"empleadoCodigo\":\"" + empleado + "\""
            + "}";

        String respuesta = enviarPeticionPOST(BASE_URL + "/cuentas/transferencia", json);

        if (respuesta == null) {
            System.out.println("❌ Error: No se pudo conectar con el servidor.\n");
        } else if (respuesta.contains("éxito") || respuesta.contains("exitosamente")) {
            System.out.println("✅ Transferencia realizada con éxito.\n");
        } else {
            System.out.println("❌ El servidor respondió con error:");
            if (respuesta.contains("Internal Server Error")) {
                System.out.println("   → Error interno del servidor (revisa el log de GlassFish).");
            } else {
                System.out.println("   → " + respuesta);
            }
            System.out.println();
        }

        // Limpiar visualmente
        System.out.println("\n".repeat(3));
        System.out.print("Presiona Enter para continuar...");
        try {
            System.in.read();
            System.out.println("\n".repeat(2));
        } catch (IOException e) {
            // Ignorar
        }
    }

    // --- MÉTODOS AUXILIARES ---

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
        String pattern = "\"" + campo + "\"\\s*:\\s*\"([^\"]*)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(jsonPart);
        if (m.find()) {
            return m.group(1);
        }
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
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    return content.toString();
                }
            } else {
                return "Error: Cuenta no encontrada.";
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
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            if (status == 200) {
                return "Operación realizada con éxito.";
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            }
        } catch (Exception e) {
            return "Error de conexión al servidor.";
        }
    }
}