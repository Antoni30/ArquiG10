using System;
using System.IO;
using System.Windows.Forms;

namespace UnitConverterDesktop
{
    public partial class LoginForm : Form
    {
        public bool IsAuthenticated { get; private set; }

        public LoginForm()
        {
            InitializeComponent();
            IsAuthenticated = false;
            // prefill user for clarity
            txtUser.Text = "MONSTER";

            // Load image dynamically: search upwards for MONSTER.jpg
            try
            {
                var imagePath = FindImageFile("MONSTER.jpg", maxParentLevels: 8);
                if (!string.IsNullOrEmpty(imagePath) && File.Exists(imagePath))
                {
                    pictureBox.Image = System.Drawing.Image.FromFile(imagePath);
                }
            }
            catch
            {
                // ignore image load errors
            }
        }

        private string FindImageFile(string fileName, int maxParentLevels = 6)
        {
            try
            {
                var dir = AppDomain.CurrentDomain.BaseDirectory;
                for (int level = 0; level <= maxParentLevels && !string.IsNullOrEmpty(dir); level++)
                {
                    // Look for the file directly in this directory and subdirectories (limited)
                    try
                    {
                        var matches = Directory.GetFiles(dir, fileName, SearchOption.AllDirectories);
                        if (matches != null && matches.Length > 0)
                        {
                            return matches[0];
                        }
                    }
                    catch { /* ignore access errors for some folders */ }

                    // move up one directory
                    var parent = Directory.GetParent(dir);
                    dir = parent?.FullName;
                }
            }
            catch
            {
                // swallow
            }

            return string.Empty;
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {
            var user = txtUser.Text?.Trim();
            var pass = txtPass.Text ?? string.Empty;

            // hardcoded credentials
            if (string.Equals(user, "MONSTER", StringComparison.OrdinalIgnoreCase) && pass == "MONSTER9")
            {
                IsAuthenticated = true;
                this.DialogResult = DialogResult.OK;
                this.Close();
            }
            else
            {
                MessageBox.Show("Usuario o contraseña incorrectos", "Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            IsAuthenticated = false;
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }
    }
}
