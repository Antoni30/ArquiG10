using System;
using System.Data.SqlClient;
using System.Drawing;
using System.IO;
using System.Windows.Forms;

namespace ConversionUnidadesDesktop
{
    public partial class LoginForm : Form
    {
        public bool Autenticado { get; private set; }

        public LoginForm()
        {
            InitializeComponent();
            this.StartPosition = FormStartPosition.CenterScreen;
            this.Text = "Login";

            // Cargar imagen
            try
            {
                pbLogo.BackColor = Color.DarkBlue;
                string imagePath = @"C:\Users\USER\Desktop\Nuevo Carpeta\ArquiG10\TI1.2 SOAP_DOTNET_SIN_BD_GR10\02. CLIESC\Resources\MONSTER.jpg";

                if (File.Exists(imagePath))
                {
                    pbLogo.Image = Image.FromFile(imagePath); // ← Esto es obligatorio
                }
                else
                {
                    throw new FileNotFoundException("La imagen no se encontró en la ruta especificada.");
                }

                pbLogo.SizeMode = PictureBoxSizeMode.Zoom;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar la imagen: " + ex.Message);
                pbLogo.BackColor = Color.DarkBlue;
                pbLogo.Image = null;
            }

            lblError.Visible = false;
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {
            if (txtUsuario.Text == "MONSTER" && txtPassword.Text == "MONSTER9")
            {
                Autenticado = true;
                this.Close();
            }
            else
            {
                lblError.Text = "Credenciales incorrectas. Intente de nuevo.";
                lblError.Visible = true;
                txtPassword.Clear();
                txtUsuario.Focus();
            }
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}