using System.Windows.Forms;

namespace ConversionUnidadesDesktop
{
    partial class MainForm
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.tabControl = new System.Windows.Forms.TabControl();
            this.tabLongitud = new System.Windows.Forms.TabPage();
            this.lblResultadoLongitud = new System.Windows.Forms.Label();
            this.cmbLongitud = new System.Windows.Forms.ComboBox();
            this.lblLongitud = new System.Windows.Forms.Label();
            this.txtValorLongitud = new System.Windows.Forms.TextBox();
            this.lblValorLongitud = new System.Windows.Forms.Label();
            this.tabMasa = new System.Windows.Forms.TabPage();
            this.lblResultadoMasa = new System.Windows.Forms.Label();
            this.cmbMasa = new System.Windows.Forms.ComboBox();
            this.lblMasa = new System.Windows.Forms.Label();
            this.txtValorMasa = new System.Windows.Forms.TextBox();
            this.lblValorMasa = new System.Windows.Forms.Label();
            this.tabTemperatura = new System.Windows.Forms.TabPage();
            this.lblResultadoTemperatura = new System.Windows.Forms.Label();
            this.cmbTemperatura = new System.Windows.Forms.ComboBox();
            this.lblTemperatura = new System.Windows.Forms.Label();
            this.txtValorTemperatura = new System.Windows.Forms.TextBox();
            this.lblValorTemperatura = new System.Windows.Forms.Label();
            this.btnCerrarSesion = new System.Windows.Forms.Button();
            this.tabControl.SuspendLayout();
            this.tabLongitud.SuspendLayout();
            this.tabMasa.SuspendLayout();
            this.tabTemperatura.SuspendLayout();
            this.SuspendLayout();

            // 
            // tabControl
            // 
            this.tabControl.Controls.Add(this.tabLongitud);
            this.tabControl.Controls.Add(this.tabMasa);
            this.tabControl.Controls.Add(this.tabTemperatura);
            this.tabControl.Dock = System.Windows.Forms.DockStyle.Top;
            this.tabControl.Location = new System.Drawing.Point(0, 0);
            this.tabControl.Name = "tabControl";
            this.tabControl.SelectedIndex = 0;
            this.tabControl.Size = new System.Drawing.Size(800, 600);
            this.tabControl.TabIndex = 0;

            // 
            // tabLongitud
            // 
            this.tabLongitud.Controls.Add(this.lblResultadoLongitud);
            this.tabLongitud.Controls.Add(this.cmbLongitud);
            this.tabLongitud.Controls.Add(this.lblLongitud);
            this.tabLongitud.Controls.Add(this.txtValorLongitud);
            this.tabLongitud.Controls.Add(this.lblValorLongitud);
            this.tabLongitud.Location = new System.Drawing.Point(4, 25);
            this.tabLongitud.Name = "tabLongitud";
            this.tabLongitud.Padding = new System.Windows.Forms.Padding(3);
            this.tabLongitud.Size = new System.Drawing.Size(792, 571);
            this.tabLongitud.TabIndex = 0;
            this.tabLongitud.Text = "📏 Longitud";
            this.tabLongitud.UseVisualStyleBackColor = true;

            // 
            // lblResultadoLongitud
            // 
            this.lblResultadoLongitud.BackColor = System.Drawing.Color.LightGreen;
            this.lblResultadoLongitud.Font = new System.Drawing.Font("Segoe UI", 10F, System.Drawing.FontStyle.Bold);
            this.lblResultadoLongitud.Location = new System.Drawing.Point(60, 250);
            this.lblResultadoLongitud.Name = "lblResultadoLongitud";
            this.lblResultadoLongitud.Padding = new System.Windows.Forms.Padding(10);
            this.lblResultadoLongitud.Size = new System.Drawing.Size(600, 50);
            this.lblResultadoLongitud.TabIndex = 5;
            this.lblResultadoLongitud.Text = "Resultado aparecerá aquí...";
            this.lblResultadoLongitud.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

            // 
            // cmbLongitud
            // 
            this.cmbLongitud.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbLongitud.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.cmbLongitud.FormattingEnabled = true;
            this.cmbLongitud.Location = new System.Drawing.Point(60, 200);
            this.cmbLongitud.Name = "cmbLongitud";
            this.cmbLongitud.Size = new System.Drawing.Size(300, 31);
            this.cmbLongitud.TabIndex = 4;
            this.cmbLongitud.SelectedIndexChanged += new System.EventHandler(this.cmbConversion_SelectedIndexChanged);

            // 
            // lblLongitud
            // 
            this.lblLongitud.AutoSize = true;
            this.lblLongitud.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblLongitud.Location = new System.Drawing.Point(57, 175);
            this.lblLongitud.Name = "lblLongitud";
            this.lblLongitud.Size = new System.Drawing.Size(232, 23);
            this.lblLongitud.TabIndex = 3;
            this.lblLongitud.Text = "Seleccionar Conversión (Tipo: Longitud):";

            // 
            // txtValorLongitud
            // 
            this.txtValorLongitud.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.txtValorLongitud.Location = new System.Drawing.Point(60, 120);
            this.txtValorLongitud.Name = "txtValorLongitud";
            this.txtValorLongitud.Size = new System.Drawing.Size(300, 30);
            this.txtValorLongitud.TabIndex = 2;

            // 
            // lblValorLongitud
            // 
            this.lblValorLongitud.AutoSize = true;
            this.lblValorLongitud.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblValorLongitud.Location = new System.Drawing.Point(57, 95);
            this.lblValorLongitud.Name = "lblValorLongitud";
            this.lblValorLongitud.Size = new System.Drawing.Size(130, 23);
            this.lblValorLongitud.TabIndex = 1;
            this.lblValorLongitud.Text = "Valor a Convertir:";

            // 
            // tabMasa
            // 
            this.tabMasa.Controls.Add(this.lblResultadoMasa);
            this.tabMasa.Controls.Add(this.cmbMasa);
            this.tabMasa.Controls.Add(this.lblMasa);
            this.tabMasa.Controls.Add(this.txtValorMasa);
            this.tabMasa.Controls.Add(this.lblValorMasa);
            this.tabMasa.Location = new System.Drawing.Point(4, 25);
            this.tabMasa.Name = "tabMasa";
            this.tabMasa.Padding = new System.Windows.Forms.Padding(3);
            this.tabMasa.Size = new System.Drawing.Size(792, 571);
            this.tabMasa.TabIndex = 1;
            this.tabMasa.Text = "⚖️ Masa";
            this.tabMasa.UseVisualStyleBackColor = true;

            // 
            // lblResultadoMasa
            // 
            this.lblResultadoMasa.BackColor = System.Drawing.Color.LightGreen;
            this.lblResultadoMasa.Font = new System.Drawing.Font("Segoe UI", 10F, System.Drawing.FontStyle.Bold);
            this.lblResultadoMasa.Location = new System.Drawing.Point(60, 250);
            this.lblResultadoMasa.Name = "lblResultadoMasa";
            this.lblResultadoMasa.Padding = new System.Windows.Forms.Padding(10);
            this.lblResultadoMasa.Size = new System.Drawing.Size(600, 50);
            this.lblResultadoMasa.TabIndex = 5;
            this.lblResultadoMasa.Text = "Resultado aparecerá aquí...";
            this.lblResultadoMasa.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

            // 
            // cmbMasa
            // 
            this.cmbMasa.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbMasa.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.cmbMasa.FormattingEnabled = true;
            this.cmbMasa.Location = new System.Drawing.Point(60, 200);
            this.cmbMasa.Name = "cmbMasa";
            this.cmbMasa.Size = new System.Drawing.Size(300, 31);
            this.cmbMasa.TabIndex = 4;
            this.cmbMasa.SelectedIndexChanged += new System.EventHandler(this.cmbConversion_SelectedIndexChanged);

            // 
            // lblMasa
            // 
            this.lblMasa.AutoSize = true;
            this.lblMasa.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblMasa.Location = new System.Drawing.Point(57, 175);
            this.lblMasa.Name = "lblMasa";
            this.lblMasa.Size = new System.Drawing.Size(225, 23);
            this.lblMasa.TabIndex = 3;
            this.lblMasa.Text = "Seleccionar Conversión (Tipo: Masa):";

            // 
            // txtValorMasa
            // 
            this.txtValorMasa.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.txtValorMasa.Location = new System.Drawing.Point(60, 120);
            this.txtValorMasa.Name = "txtValorMasa";
            this.txtValorMasa.Size = new System.Drawing.Size(300, 30);
            this.txtValorMasa.TabIndex = 2;

            // 
            // lblValorMasa
            // 
            this.lblValorMasa.AutoSize = true;
            this.lblValorMasa.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblValorMasa.Location = new System.Drawing.Point(57, 95);
            this.lblValorMasa.Name = "lblValorMasa";
            this.lblValorMasa.Size = new System.Drawing.Size(130, 23);
            this.lblValorMasa.TabIndex = 1;
            this.lblValorMasa.Text = "Valor a Convertir:";

            // 
            // tabTemperatura
            // 
            this.tabTemperatura.Controls.Add(this.lblResultadoTemperatura);
            this.tabTemperatura.Controls.Add(this.cmbTemperatura);
            this.tabTemperatura.Controls.Add(this.lblTemperatura);
            this.tabTemperatura.Controls.Add(this.txtValorTemperatura);
            this.tabTemperatura.Controls.Add(this.lblValorTemperatura);
            this.tabTemperatura.Location = new System.Drawing.Point(4, 25);
            this.tabTemperatura.Name = "tabTemperatura";
            this.tabTemperatura.Padding = new System.Windows.Forms.Padding(3);
            this.tabTemperatura.Size = new System.Drawing.Size(792, 571);
            this.tabTemperatura.TabIndex = 2;
            this.tabTemperatura.Text = "🌡️ Temperatura";
            this.tabTemperatura.UseVisualStyleBackColor = true;

            // 
            // lblResultadoTemperatura
            // 
            this.lblResultadoTemperatura.BackColor = System.Drawing.Color.LightGreen;
            this.lblResultadoTemperatura.Font = new System.Drawing.Font("Segoe UI", 10F, System.Drawing.FontStyle.Bold);
            this.lblResultadoTemperatura.Location = new System.Drawing.Point(60, 250);
            this.lblResultadoTemperatura.Name = "lblResultadoTemperatura";
            this.lblResultadoTemperatura.Padding = new System.Windows.Forms.Padding(10);
            this.lblResultadoTemperatura.Size = new System.Drawing.Size(600, 50);
            this.lblResultadoTemperatura.TabIndex = 5;
            this.lblResultadoTemperatura.Text = "Resultado aparecerá aquí...";
            this.lblResultadoTemperatura.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

            // 
            // cmbTemperatura
            // 
            this.cmbTemperatura.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbTemperatura.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.cmbTemperatura.FormattingEnabled = true;
            this.cmbTemperatura.Location = new System.Drawing.Point(60, 200);
            this.cmbTemperatura.Name = "cmbTemperatura";
            this.cmbTemperatura.Size = new System.Drawing.Size(300, 31);
            this.cmbTemperatura.TabIndex = 4;
            this.cmbTemperatura.SelectedIndexChanged += new System.EventHandler(this.cmbConversion_SelectedIndexChanged);

            // 
            // lblTemperatura
            // 
            this.lblTemperatura.AutoSize = true;
            this.lblTemperatura.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblTemperatura.Location = new System.Drawing.Point(57, 175);
            this.lblTemperatura.Name = "lblTemperatura";
            this.lblTemperatura.Size = new System.Drawing.Size(257, 23);
            this.lblTemperatura.TabIndex = 3;
            this.lblTemperatura.Text = "Seleccionar Conversión (Tipo: Temperatura):";

            // 
            // txtValorTemperatura
            // 
            this.txtValorTemperatura.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.txtValorTemperatura.Location = new System.Drawing.Point(60, 120);
            this.txtValorTemperatura.Name = "txtValorTemperatura";
            this.txtValorTemperatura.Size = new System.Drawing.Size(300, 30);
            this.txtValorTemperatura.TabIndex = 2;

            // 
            // lblValorTemperatura
            // 
            this.lblValorTemperatura.AutoSize = true;
            this.lblValorTemperatura.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.lblValorTemperatura.Location = new System.Drawing.Point(57, 95);
            this.lblValorTemperatura.Name = "lblValorTemperatura";
            this.lblValorTemperatura.Size = new System.Drawing.Size(130, 23);
            this.lblValorTemperatura.TabIndex = 1;
            this.lblValorTemperatura.Text = "Valor a Convertir:";

            // 
            // btnCerrarSesion
            // 
            this.btnCerrarSesion.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(128)))), ((int)(((byte)(128)))));
            this.btnCerrarSesion.FlatAppearance.BorderSize = 0;
            this.btnCerrarSesion.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnCerrarSesion.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold);
            this.btnCerrarSesion.Location = new System.Drawing.Point(700, 10);
            this.btnCerrarSesion.Name = "btnCerrarSesion";
            this.btnCerrarSesion.Size = new System.Drawing.Size(80, 30);
            this.btnCerrarSesion.TabIndex = 1;
            this.btnCerrarSesion.Text = "Cerrar Sesión";
            this.btnCerrarSesion.UseVisualStyleBackColor = false;
            this.btnCerrarSesion.Click += new System.EventHandler(this.btnCerrarSesion_Click);

            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 600);
            this.Controls.Add(this.btnCerrarSesion);
            this.Controls.Add(this.tabControl);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Web Service de Conversiones (SOAP)";
            this.tabControl.ResumeLayout(false);
            this.tabLongitud.ResumeLayout(false);
            this.tabLongitud.PerformLayout();
            this.tabMasa.ResumeLayout(false);
            this.tabMasa.PerformLayout();
            this.tabTemperatura.ResumeLayout(false);
            this.tabTemperatura.PerformLayout();
            this.ResumeLayout(false);
        }

        private TabControl tabControl;
        private TabPage tabLongitud;
        private Label lblResultadoLongitud;
        private ComboBox cmbLongitud;
        private Label lblLongitud;
        private TextBox txtValorLongitud;
        private Label lblValorLongitud;
        private TabPage tabMasa;
        private Label lblResultadoMasa;
        private ComboBox cmbMasa;
        private Label lblMasa;
        private TextBox txtValorMasa;
        private Label lblValorMasa;
        private TabPage tabTemperatura;
        private Label lblResultadoTemperatura;
        private ComboBox cmbTemperatura;
        private Label lblTemperatura;
        private TextBox txtValorTemperatura;
        private Label lblValorTemperatura;
        private Button btnCerrarSesion;
    }
}