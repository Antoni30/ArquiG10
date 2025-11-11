using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WS_EUREKA_CLICON.Controller;
using WS_EUREKA_CLICON.View;

namespace WS_EUREKA_CLICON
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var controller = new EurekaController();
            var view = new EurekaView();

            // Solicitar usuario y contraseña
            Console.WriteLine("Ingrese su usuario:");
            string usuario = Console.ReadLine();

            Console.WriteLine("Ingrese su contraseña:");
            string password = LeerContraseña();

            // Validar usuario
            bool esValido = controller.ValidarUsuario(usuario, password);
            if (esValido)
            {
                Console.WriteLine("Ingreso correcto.");
                MostrarMenu(controller, view);
            }
            else
            {
                Console.WriteLine("Usuario o contraseña incorrectos.");
            }

            Console.WriteLine("Presione cualquier tecla para salir...");
            Console.ReadKey();
        }

        // Función para leer la contraseña de forma oculta
        private static string LeerContraseña()
        {
            string password = "";
            ConsoleKeyInfo tecla;

            while (true)
            {
                tecla = Console.ReadKey(true); // Lee la tecla sin mostrarla en pantalla

                if (tecla.Key == ConsoleKey.Enter) // Si se presiona Enter, salimos
                {
                    break;
                }
                else if (tecla.Key == ConsoleKey.Backspace && password.Length > 0) // Si se presiona Backspace, eliminamos un carácter
                {
                    password = password.Substring(0, password.Length - 1);
                    Console.Write("\b \b"); // Borra el último carácter de la consola
                }
                else if (!char.IsControl(tecla.KeyChar)) // Si la tecla es un carácter válido, lo añadimos
                {
                    password += tecla.KeyChar;
                    Console.Write("*"); // Muestra un asterisco en lugar del carácter
                }
            }

            Console.WriteLine(); // Para mover a la siguiente línea después de la contraseña
            return password;
        }

        // Mostrar el menú de opciones después de validarse
        private static void MostrarMenu(EurekaController controller, EurekaView view)
        {
            bool salir = false;
            while (!salir)
            {
                Console.Clear(); // Limpiar la consola para mostrar el menú nuevamente

                Console.WriteLine("\nSeleccione una opción:");
                Console.WriteLine("1. Ver movimientos");
                Console.WriteLine("2. Realizar depósito");
                Console.WriteLine("3. Realizar retiro");
                Console.WriteLine("4. Realizar transferencia");
                Console.WriteLine("5. Salir");

                string opcion = Console.ReadLine();

                switch (opcion)
                {
                    case "1":
                        VerMovimientos(controller, view);
                        break;
                    case "2":
                        RealizarDeposito(controller, view);
                        break;
                    case "3":
                        RealizarRetiro(controller, view);
                        break;
                    case "4":
                        RealizarTransferencia(controller, view);
                        break;
                    case "5":
                        Console.WriteLine("Saliendo...");
                        salir = true; // Salir del bucle y terminar el programa
                        break;
                    default:
                        Console.WriteLine("Opción no válida. Intente nuevamente.");
                        break;
                }
            }
        }

        // Ver los movimientos de la cuenta
        private static void VerMovimientos(EurekaController controller, EurekaView view)
        {
            Console.WriteLine("Ingrese el número de cuenta para ver los movimientos:");
            string cuenta = Console.ReadLine();

            var movimientos = controller.ObtenerMovimientos(cuenta);
            view.MostrarMovimientos(movimientos);

            Console.WriteLine("\nPresione cualquier tecla para volver al menú...");
            Console.ReadKey();
        }

        // Realizar un depósito
        private static void RealizarDeposito(EurekaController controller, EurekaView view)
        {
            Console.WriteLine("Ingrese el número de cuenta para realizar el depósito:");
            string cuenta = Console.ReadLine();

            Console.WriteLine("Ingrese el monto del depósito:");
            if (double.TryParse(Console.ReadLine(), out double importe) && importe > 0)
            {
                bool exito = controller.RegistrarDeposito(cuenta, importe);
                view.MostrarResultadoDeposito(exito);
            }
            else
            {
                Console.WriteLine("Monto no válido.");
            }

            Console.WriteLine("\nPresione cualquier tecla para volver al menú...");
            Console.ReadKey();
        }

        private static void RealizarRetiro(EurekaController controller, EurekaView view)
        {
            Console.WriteLine("Ingrese el número de cuenta para realizar el retiro:");
            string cuenta = Console.ReadLine();

            Console.WriteLine("Ingrese el monto del retiro:");
            if (double.TryParse(Console.ReadLine(), out double importe) && importe > 0)
            {
                bool exito = controller.RegistrarRetiro(cuenta, importe);
                view.MostrarResultadoRetiro(exito);
            }
            else
            {
                Console.WriteLine("Monto no válido.");
            }

            Console.WriteLine("\nPresione cualquier tecla para volver al menú...");
            Console.ReadKey();
        }

        private static void RealizarTransferencia(EurekaController controller, EurekaView view)
        {
            Console.WriteLine("Ingrese el número de cuenta para realizar el transferencia:");
            string cuenta = Console.ReadLine();

            Console.WriteLine("Ingrese el monto del transferencia:");
            if (double.TryParse(Console.ReadLine(), out double importe) && importe > 0)
            {
                bool exito = controller.RegistrarTransferencia(cuenta, importe);
                view.MostrarResultadoTransferencia(exito);
            }
            else
            {
                Console.WriteLine("Monto no válido.");
            }

            Console.WriteLine("\nPresione cualquier tecla para volver al menú...");
            Console.ReadKey();
        }
    }
}
