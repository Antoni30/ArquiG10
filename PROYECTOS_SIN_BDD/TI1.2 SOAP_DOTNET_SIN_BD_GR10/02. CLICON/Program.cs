using System;
using ConversionUnidades.ConversionServiceReference; // Ajusta si el namespace es diferente

namespace ConversionUnidades
{
    internal class Program
    {
        // Usuario y contraseña quemados (hardcoded)
        private const string USUARIO_CORRECTO = "MONSTER";
        private const string CONTRASENA_CORRECTA = "MONSTER9";

        static void Main(string[] args)
        {
            Console.WriteLine("=== LOGIN ===");
            if (!AutenticarUsuario())
            {
                Console.WriteLine("\n❌ Credenciales incorrectas. Saliendo...");
                Console.ReadKey();
                return;
            }

            Console.WriteLine("\n✅ Login exitoso. Bienvenido MONSTER!\n");

            using (var client = new ConversionServiceClient())
            {
                bool salir = false;
                while (!salir)
                {
                    MostrarMenu();
                    string opcion = Console.ReadLine();

                    switch (opcion)
                    {
                        case "1":
                            EjecutarConversion(client, "Centímetros a Metros",
                                "Ingresa los centímetros: ",
                                client.CentimetrosAMetros);
                            break;
                        case "2":
                            EjecutarConversion(client, "Metros a Centímetros",
                                "Ingresa los metros: ",
                                client.MetrosACentimetros);
                            break;
                        case "3":
                            EjecutarConversion(client, "Metros a Kilómetros",
                                "Ingresa los metros: ",
                                client.MetrosAKilometros);
                            break;
                        case "4":
                            EjecutarConversion(client, "Toneladas a Libras",
                                "Ingresa las toneladas: ",
                                client.ToneladaALibra);
                            break;
                        case "5":
                            EjecutarConversion(client, "Kilogramos a Libras",
                                "Ingresa los kilogramos: ",
                                client.KilogramoALibra);
                            break;
                        case "6":
                            EjecutarConversion(client, "Gramos a Kilogramos",
                                "Ingresa los gramos: ",
                                client.GramoAKilogramo);
                            break;
                        case "7":
                            EjecutarConversion(client, "Celsius a Fahrenheit",
                                "Ingresa los grados Celsius: ",
                                client.CelsiusAFahrenheit);
                            break;
                        case "8":
                            EjecutarConversion(client, "Fahrenheit a Celsius",
                                "Ingresa los grados Fahrenheit: ",
                                client.FahrenheitACelsius);
                            break;
                        case "9":
                            EjecutarConversion(client, "Celsius a Kelvin",
                                "Ingresa los grados Celsius: ",
                                client.CelsiusAKelvin);
                            break;
                        case "0": // Cambié de 10 a 0 para seguir el ejemplo visual
                            salir = true;
                            Console.WriteLine("¡Gracias por usar el conversor! 👋");
                            break;
                        default:
                            Console.WriteLine("❌ Opción inválida. Por favor, elige del 1 al 9 o 0 para salir.\n");
                            break;
                    }

                    if (!salir)
                    {
                        Console.WriteLine("\nPresiona cualquier tecla para continuar...");
                        Console.ReadKey();
                        Console.Clear();
                    }
                }
            }
        }

        static bool AutenticarUsuario()
        {
            Console.Write("Usuario: ");
            string usuario = Console.ReadLine();

            Console.Write("Contraseña: ");
            string contrasena = LeerContrasena(); // Para ocultar la contraseña mientras se escribe

            return usuario == USUARIO_CORRECTO && contrasena == CONTRASENA_CORRECTA;
        }

        static string LeerContrasena()
        {
            string password = "";
            ConsoleKeyInfo key;

            do
            {
                key = Console.ReadKey(true);

                if (key.Key != ConsoleKey.Backspace && key.Key != ConsoleKey.Enter)
                {
                    password += key.KeyChar;
                    Console.Write("*");
                }
                else if (key.Key == ConsoleKey.Backspace && password.Length > 0)
                {
                    password = password.Substring(0, password.Length - 1);
                    Console.Write("\b \b");
                }
            } while (key.Key != ConsoleKey.Enter);

            Console.WriteLine();
            return password;
        }

        static void MostrarMenu()
        {
            Console.WriteLine("=== MENÚ DE CONVERSIONES ===");
            Console.WriteLine("1. Centímetros a Metros");
            Console.WriteLine("2. Metros a Centímetros");
            Console.WriteLine("3. Metros a Kilómetros");
            Console.WriteLine("4. Toneladas a Libras");
            Console.WriteLine("5. Kilogramos a Libras");
            Console.WriteLine("6. Gramos a Kilogramos");
            Console.WriteLine("7. Celsius a Fahrenheit");
            Console.WriteLine("8. Fahrenheit a Celsius");
            Console.WriteLine("9. Celsius a Kelvin");
            Console.WriteLine("0. Salir");
            Console.Write("\nSelecciona una opción: ");
        }

        static void EjecutarConversion(
            ConversionServiceClient client,
            string nombreConversion,
            string mensajeEntrada,
            Func<double, double> metodo)
        {
            try
            {
                Console.WriteLine($"\n--- {nombreConversion} ---");
                Console.Write(mensajeEntrada);
                string input = Console.ReadLine();

                if (double.TryParse(input, out double valor))
                {
                    double resultado = metodo(valor);
                    Console.WriteLine($"✅ Resultado: {resultado:F4}");
                }
                else
                {
                    Console.WriteLine("❌ Entrada no válida. Debe ser un número.");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"⚠️ Error al comunicarse con el servicio: {ex.Message}");
            }
        }
    }
}