using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace UnitConverterClient
{
    class Program
    {
        private static HttpClient _httpClient;
        private static readonly string _baseUrl = "https://localhost:7258/api/Conversion/";

        // Clase ApiResponse local para evitar ambigüedades
        public class ApiResponse<T>
        {
            public bool Success { get; set; }
            public string Message { get; set; } = string.Empty;
            public T Data { get; set; }
            public DateTime Timestamp { get; set; }
        }

        static async Task Main(string[] args)
        {
            // Configurar HttpClient para ignorar certificados SSL (solo desarrollo)
            var handler = new HttpClientHandler();
            handler.ServerCertificateCustomValidationCallback = (message, cert, chain, errors) => true;
            _httpClient = new HttpClient(handler);

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
            bool isConnected = await HealthCheckAsync();
            if (!isConnected)
            {
                Console.WriteLine("❌ No se puede conectar al servidor. Asegúrate de que:");
                Console.WriteLine("   1. El servidor UnitConverterAPI esté ejecutándose");
                Console.WriteLine("   2. La URL https://localhost:7258 esté accesible");
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

        static async Task<bool> HealthCheckAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync(_baseUrl.Replace("Conversion/", "Conversion/health"));
                return response.IsSuccessStatusCode;
            }
            catch
            {
                return false;
            }
        }

        static async Task<T> PostAsync<T>(string endpoint, double value)
        {
            var json = JsonSerializer.Serialize(value);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await _httpClient.PostAsync(_baseUrl + endpoint, content);

            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                var apiResponse = JsonSerializer.Deserialize<ApiResponse<T>>(
                    responseContent,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                );

                if (apiResponse?.Success == true)
                {
                    return apiResponse.Data;
                }
                else
                {
                    throw new Exception($"Error del servidor: {apiResponse?.Message}");
                }
            }
            else
            {
                throw new Exception($"Error HTTP: {response.StatusCode}");
            }
        }

        static async Task ConvertirCentimetrosAMetros()
        {
            Console.Write("Ingresa los centímetros: ");
            if (double.TryParse(Console.ReadLine(), out double cm))
            {
                var resultado = await PostAsync<double>("centimetros-a-metros", cm);
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
                var resultado = await PostAsync<double>("metros-a-centimetros", m);
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
                var resultado = await PostAsync<double>("metros-a-kilometros", m);
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
                var resultado = await PostAsync<double>("tonelada-a-libra", ton);
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
                var resultado = await PostAsync<double>("kilogramo-a-libra", kg);
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
                var resultado = await PostAsync<double>("gramo-a-kilogramo", g);
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
                var resultado = await PostAsync<double>("celsius-a-fahrenheit", c);
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
                var resultado = await PostAsync<double>("fahrenheit-a-celsius", f);
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
                var resultado = await PostAsync<double>("celsius-a-kelvin", c);
                Console.WriteLine($"✅ {c}°C = {resultado} K");
            }
            else
            {
                Console.WriteLine("❌ Valor no válido");
            }
        }
    }
}