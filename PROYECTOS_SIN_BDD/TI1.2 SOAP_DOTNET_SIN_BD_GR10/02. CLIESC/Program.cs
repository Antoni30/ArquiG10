using System;
using System.Windows.Forms;

namespace ConversionUnidadesDesktop
{
    internal static class Program
    {
        /// <summary>
        /// Punto de entrada principal para la aplicación.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            // Mostrar login de forma modal
            var loginForm = new LoginForm();
            var result = loginForm.ShowDialog();

            // Si el login fue exitoso, abrir la aplicación principal
            if (loginForm.Autenticado)
            {
                Application.Run(new MainForm());
            }
            else
            {
                // Si cancela o falla, salir
                Application.Exit();
            }
        }
    }
}