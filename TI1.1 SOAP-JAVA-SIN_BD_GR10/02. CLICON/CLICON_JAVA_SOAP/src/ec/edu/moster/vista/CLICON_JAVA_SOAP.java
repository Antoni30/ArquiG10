package ec.edu.moster.vista;

import ec.edu.monster.utils.Login;
import ec.edu.monster.utils.Menu;
import ec.edu.moster.servicios.WSConversionUnidades_Service;
import ec.edu.moster.servicios.WSConversionUnidades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class CLICON_JAVA_SOAP {

    public static void main(String[] args) throws IOException {
        Login login = new Login();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Menu men = new Menu();
        WSConversionUnidades_Service service = new WSConversionUnidades_Service();
        WSConversionUnidades port = service.getWSConversionUnidadesPort();
        double resultado;
        int opcion = 0;
        DecimalFormat df = new DecimalFormat("#.####"); // formatea a 4 decimales

        if (!login.login(br)) {
            System.out.println("\nDemasiados intentos. Programa finalizado.");
            return;
        }

        do {
            men.menu();
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Opción inválida. Intente nuevamente.");
                continue;
            }
            if (opcion == 0) {
                System.out.println("👋 Saliendo del programa...");
                break;
            }

            System.out.print("Ingrese el valor a convertir: ");
            double valor;
            try {
                valor = Double.parseDouble(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Valor inválido. Intente nuevamente.");
                continue;
            }

            try {
                String salida; // línea final a mostrar
                switch (opcion) {
                    case 1:
                        // Centímetros -> Metros
                        resultado = port.centimetrosAMetros(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " cm  ->  " + df.format(resultado) + " m";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 2:
                        // Metros -> Centímetros
                        resultado = port.metrosACentimetros(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " m   ->  " + df.format(resultado) + " cm";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 3:
                        // Metros -> Kilómetros
                        resultado = port.metrosAKilometros(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " m   ->  " + df.format(resultado) + " km";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 4:
                        // Toneladas -> Libras
                        resultado = port.toneladaALibra(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " t   ->  " + df.format(resultado) + " lb";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 5:
                        // Kilogramo -> Libra
                        resultado = port.kilogramoALibra(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " kg  ->  " + df.format(resultado) + " lb";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 6:
                        // Gramo -> Kilogramo
                        resultado = port.gramoAKilogramo(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos.");
                            continue;
                        }
                        salida = df.format(valor) + " g   ->  " + df.format(resultado) + " kg";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 7:
                        // Celsius -> Fahrenheit
                        resultado = port.celsiusAFahrenheit(valor);
                        // Para temperatura dejamos que el servicio valide Kelvin internamente;
                        // si tu servicio usa -1 para errores, lo manejamos igual.
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos (o valor fuera de rango).");
                            continue;
                        }
                        salida = df.format(valor) + " °C  ->  " + df.format(resultado) + " °F";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 8:
                        // Fahrenheit -> Celsius
                        resultado = port.fahrenheitACelsius(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos (o valor fuera de rango).");
                            continue;
                        }
                        salida = df.format(valor) + " °F  ->  " + df.format(resultado) + " °C";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    case 9:
                        // Celsius -> Kelvin
                        resultado = port.celsiusAKelvin(valor);
                        if (resultado == -1) {
                            System.out.println("❌ No se permiten valores negativos (o valor fuera de rango).");
                            continue;
                        }
                        salida = df.format(valor) + " °C  ->  " + df.format(resultado) + " K";
                        System.out.println("✅ Resultado: " + salida);
                        break;

                    default:
                        System.out.println("Opción no válida.");
                        break;
                }

            } catch (Exception ex) {
                System.out.println("⚠️ Error llamando al servicio: " + ex.getMessage());
                ex.printStackTrace();
            }

        } while (opcion != 0);
    }

}
