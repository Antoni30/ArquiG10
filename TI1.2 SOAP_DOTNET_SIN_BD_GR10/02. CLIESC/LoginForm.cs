using System;
using System.Data.SqlClient;
using System.Drawing;
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
                pbLogo.Image = null;
                //pbLogo.Image = Properties.Resources.equipo; // Asegúrate que el recurso se llama así
            }
            catch
            {
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