using System;
using System.Net.Http;
using System.Threading.Tasks;

namespace UnitConverterClient
{
    class Program
    {
        private static ConversionService _conversionService;

        static async Task Main(string[] args)
        {
            // Configurar HttpClient para ignorar certificados SSL (solo desarrollo)
            var handler = new HttpClientHandler();
            handler.ServerCertificateCustomValidationCallback = (message, cert, chain, errors) => true;

            var httpClient = new HttpClient(handler);
            var apiClient = new ApiClient(httpClient);
            _conversionService = new ConversionService(apiClient);

            // Mostrar pantalla de login
            if (!await MostrarLogin())
            {
                Console.WriteLine("Acceso denegado. Presiona cualquier tecla para salir...");
                Console.ReadKey();
                return;
            }

            Console.Clear();
            Console.WriteLine("=== CONVERSOR DE UNIDADES ===\n");

            // Verificar conexión con el servidor
            bool isConnected = await apiClient.HealthCheckAsync();
            if (!isConnected)
            {
                Console.WriteLine("❌ No se puede conectar al servidor. Asegúrate de que:");
                Console.WriteLine("   1. El servidor UnitConverterAPI esté ejecutándose");
                Console.WriteLine("   2. La URL http://10.40.32.159:5000 esté accesible");
                Console.WriteLine("\nPresiona cualquier tecla para salir...");
                Console.ReadKey();
                return;
            }

            Console.WriteLine("✅ Conectado al servidor correctamente\n");

            bool salir = false;

            while (!salir)
            {
                MostrarMenu();
                var opcion = Console.ReadLine();

                try
                {
                    switch (opcion)
                    {
                        case "1":
                            await ConvertirCentimetrosAMetros();
                            break;
                        case "2":
                            await ConvertirMetrosACentimetros();
                            break;
                        case "3":
                            await ConvertirMetrosAKilometros();
                            break;
                        case "4":
                            await ConvertirToneladaALibra();
                            break;
                        case "5":
                            await ConvertirKilogramoALibra();
                            break;
                        case "6":
                            await ConvertirGramoAKilogramo();
                            break;
                        case "7":
                            await ConvertirCelsiusAFahrenheit();
                            break;
                        case "8":
                            await ConvertirFahrenheitACelsius();
                            break;
                        case "9":
                            await ConvertirCelsiusAKelvin();
                            break;
                        case "10":
                            salir = true;
                            Console.WriteLine("¡Hasta luego!");
                            break;
                        default:
                            Console.WriteLine("❌ Opción no válida. Intenta nuevamente.\n");
                            break;
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"❌ Error: {ex.Message}\n");
                }

                if (!salir && opcion != "10")
                {
                    Console.WriteLine("\nPresiona cualquier tecla para continuar...");
                    Console.ReadKey();
                    Console.Clear();
                    Console.WriteLine("=== CONVERSOR DE UNIDADES ===\n");
                }
            }
        }

        static async Task<bool> MostrarLogin()
        {
            Console.WriteLine("=== INICIO DE SESIÓN ===");
            Console.WriteLine("Sistema de Conversión de Unidades\n");

            int intentos = 3;

            while (intentos > 0)
            {
                Console.Write("Usuario: ");
                string usuario = Console.ReadLine();

                Console.Write("Contraseña: ");
                string contraseña = OcultarContraseña();

                Console.WriteLine(); // Salto de línea después de la contraseña

                if (usuario?.ToUpper() == "MONSTER" && contraseña == "MONSTER9")
                {
                    Console.WriteLine("✅ ¡Acceso concedido!");
                    await Task.Delay(1000); // Pequeña pausa para mostrar el mensaje
                    return true;
                }
                else
                {
                    intentos--;
                    if (intentos > 0)
                    {
                        Console.WriteLine($"❌ Credenciales incorrectas. Te quedan {intentos} intento(s).\n");
                    }
                    else
                    {
                        Console.WriteLine("❌ Has agotado todos los intentos.");
                    }
                }
            }

            return false;
        }

        static string OcultarContraseña()
        {
            string contraseña = "";
            ConsoleKeyInfo key;

            do
            {
                key = Console.ReadKey(true);

                // Si no es Enter o Backspace
                if (key.Key != ConsoleKey.Enter && key.Key != ConsoleKey.Backspace)
                {
                    contraseña += key.KeyChar;
                    Console.Write("*");
                }
                else if (key.Key == ConsoleKey.Backspace && contraseña.Length > 0)
                {
                    contraseña = contraseña.Substring(0, contraseña.Length - 1);
                    Console.Write("\b \b");
                }
            }
            while (key.Key != ConsoleKey.Enter);

            return contraseña;
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
            Console.WriteLine("10. Salir");
            Console.Write("\nSelecciona una opción: ");
        }

        static async Task ConvertirCentimetrosAMetros()
        {
            Console.Write("Ingresa los centímetros: ");
            if (double.TryParse(Console.ReadLine(), out double cm))
            {
                var resultado = await _conversionService.CentimetrosAMetros(cm);
                Console.WriteLine($"✅ {cm} cm = {resultado} m");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirMetrosACentimetros()
        {
            Console.Write("Ingresa los metros: ");
            if (double.TryParse(Console.ReadLine(), out double m))
            {
                var resultado = await _conversionService.MetrosACentimetros(m);
                Console.WriteLine($"✅ {m} m = {resultado} cm");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirMetrosAKilometros()
        {
            Console.Write("Ingresa los metros: ");
            if (double.TryParse(Console.ReadLine(), out double m))
            {
                var resultado = await _conversionService.MetrosAKilometros(m);
                Console.WriteLine($"✅ {m} m = {resultado} km");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirToneladaALibra()
        {
            Console.Write("Ingresa las toneladas: ");
            if (double.TryParse(Console.ReadLine(), out double ton))
            {
                var resultado = await _conversionService.ToneladaALibra(ton);
                Console.WriteLine($"✅ {ton} ton = {resultado} lb");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirKilogramoALibra()
        {
            Console.Write("Ingresa los kilogramos: ");
            if (double.TryParse(Console.ReadLine(), out double kg))
            {
                var resultado = await _conversionService.KilogramoALibra(kg);
                Console.WriteLine($"✅ {kg} kg = {resultado} lb");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirGramoAKilogramo()
        {
            Console.Write("Ingresa los gramos: ");
            if (double.TryParse(Console.ReadLine(), out double g))
            {
                var resultado = await _conversionService.GramoAKilogramo(g);
                Console.WriteLine($"✅ {g} g = {resultado} kg");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirCelsiusAFahrenheit()
        {
            Console.Write("Ingresa los grados Celsius: ");
            if (double.TryParse(Console.ReadLine(), out double c))
            {
                var resultado = await _conversionService.CelsiusAFahrenheit(c);
                Console.WriteLine($"✅ {c}°C = {resultado}°F");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirFahrenheitACelsius()
        {
            Console.Write("Ingresa los grados Fahrenheit: ");
            if (double.TryParse(Console.ReadLine(), out double f))
            {
                var resultado = await _conversionService.FahrenheitACelsius(f);
                Console.WriteLine($"✅ {f}°F = {resultado}°C");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }

        static async Task ConvertirCelsiusAKelvin()
        {
            Console.Write("Ingresa los grados Celsius: ");
            if (double.TryParse(Console.ReadLine(), out double c))
            {
                var resultado = await _conversionService.CelsiusAKelvin(c);
                Console.WriteLine($"✅ {c}°C = {resultado} K");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }
    }
}