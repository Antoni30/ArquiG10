using ConversionUnidadesDesktop.ConversionServiceReference;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;

namespace ConversionUnidadesDesktop
{
    public partial class MainForm : Form
    {
        private readonly ConversionServiceClient _client;

        public MainForm()
        {
            InitializeComponent();
            _client = new ConversionServiceClient();

            // Inicializar combos
            CargarCombos();
        }

        private void CargarCombos()
        {
            // Longitud
            cmbLongitud.Items.Add("-- SELECCIONE UNA OPERACIÓN --");
            cmbLongitud.Items.Add("Centímetros a Metros");
            cmbLongitud.Items.Add("Metros a Centímetros");
            cmbLongitud.Items.Add("Metros a Kilómetros");
            cmbLongitud.SelectedIndex = 0;

            // Masa
            cmbMasa.Items.Add("-- SELECCIONE UNA OPERACIÓN --");
            cmbMasa.Items.Add("Tonelada a Libra");
            cmbMasa.Items.Add("Kilogramo a Libra");
            cmbMasa.Items.Add("Gramo a Kilogramo");
            cmbMasa.SelectedIndex = 0;

            // Temperatura
            cmbTemperatura.Items.Add("-- SELECCIONE UNA OPERACIÓN --");
            cmbTemperatura.Items.Add("Celsius a Fahrenheit");
            cmbTemperatura.Items.Add("Fahrenheit a Celsius");
            cmbTemperatura.Items.Add("Celsius a Kelvin");
            cmbTemperatura.SelectedIndex = 0;
        }

        private void btnConvertir_Click(object sender, EventArgs e)
        {
            string resultado = "";
            string unidadEntrada = "", unidadSalida = "";

            try
            {
                if (tabControl.SelectedTab == tabLongitud && cmbLongitud.SelectedIndex > 0)
                {
                    if (!double.TryParse(txtValorLongitud.Text, out double valor))
                    {
                        MessageBox.Show("Por favor, ingresa un número válido en Longitud.", "Error",
                            MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        return;
                    }

                    switch (cmbLongitud.SelectedIndex)
                    {
                        case 1: // cm -> m
                            resultado = _client.CentimetrosAMetros(valor).ToString("F4");
                            unidadEntrada = "cm";
                            unidadSalida = "m";
                            break;
                        case 2: // m -> cm
                            resultado = _client.MetrosACentimetros(valor).ToString("F4");
                            unidadEntrada = "m";
                            unidadSalida = "cm";
                            break;
                        case 3: // m -> km
                            resultado = _client.MetrosAKilometros(valor).ToString("F4");
                            unidadEntrada = "m";
                            unidadSalida = "km";
                            break;
                    }
                    lblResultadoLongitud.Text = $"{valor:F2} {unidadEntrada} = {resultado} {unidadSalida}";
                    lblResultadoLongitud.BackColor = Color.LightGreen;
                }
                else if (tabControl.SelectedTab == tabMasa && cmbMasa.SelectedIndex > 0)
                {
                    if (!double.TryParse(txtValorMasa.Text, out double valor))
                    {
                        MessageBox.Show("Por favor, ingresa un número válido en Masa.", "Error",
                            MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        return;
                    }

                    switch (cmbMasa.SelectedIndex)
                    {
                        case 1: // ton -> lb
                            resultado = _client.ToneladaALibra(valor).ToString("F2");
                            unidadEntrada = "ton";
                            unidadSalida = "lb";
                            break;
                        case 2: // kg -> lb
                            resultado = _client.KilogramoALibra(valor).ToString("F2");
                            unidadEntrada = "kg";
                            unidadSalida = "lb";
                            break;
                        case 3: // g -> kg
                            resultado = _client.GramoAKilogramo(valor).ToString("F4");
                            unidadEntrada = "g";
                            unidadSalida = "kg";
                            break;
                    }
                    lblResultadoMasa.Text = $"{valor:F2} {unidadEntrada} = {resultado} {unidadSalida}";
                    lblResultadoMasa.BackColor = Color.LightGreen;
                }
                else if (tabControl.SelectedTab == tabTemperatura && cmbTemperatura.SelectedIndex > 0)
                {
                    if (!double.TryParse(txtValorTemperatura.Text, out double valor))
                    {
                        MessageBox.Show("Por favor, ingresa un número válido en Temperatura.", "Error",
                            MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        return;
                    }

                    switch (cmbTemperatura.SelectedIndex)
                    {
                        case 1: // °C -> °F
                            resultado = _client.CelsiusAFahrenheit(valor).ToString("F2");
                            unidadEntrada = "°C";
                            unidadSalida = "°F";
                            break;
                        case 2: // °F -> °C
                            resultado = _client.FahrenheitACelsius(valor).ToString("F2");
                            unidadEntrada = "°F";
                            unidadSalida = "°C";
                            break;
                        case 3: // °C -> K
                            resultado = _client.CelsiusAKelvin(valor).ToString("F2");
                            unidadEntrada = "°C";
                            unidadSalida = "K";
                            break;
                    }
                    lblResultadoTemperatura.Text = $"{valor:F2} {unidadEntrada} = {resultado} {unidadSalida}";
                    lblResultadoTemperatura.BackColor = Color.LightGreen;
                }
                else
                {
                    MessageBox.Show("Por favor, selecciona una conversión válida.", "Advertencia",
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
                    return;
                }
            }
            catch (Exception ex)
            {
                // Mostrar error en la pestaña activa
                if (tabControl.SelectedTab == tabLongitud)
                {
                    lblResultadoLongitud.Text = $"Error: {ex.Message}";
                    lblResultadoLongitud.BackColor = Color.LightPink;
                }
                else if (tabControl.SelectedTab == tabMasa)
                {
                    lblResultadoMasa.Text = $"Error: {ex.Message}";
                    lblResultadoMasa.BackColor = Color.LightPink;
                }
                else if (tabControl.SelectedTab == tabTemperatura)
                {
                    lblResultadoTemperatura.Text = $"Error: {ex.Message}";
                    lblResultadoTemperatura.BackColor = Color.LightPink;
                }
            }
        }

        private void btnCerrarSesion_Click(object sender, EventArgs e)
        {
            _client?.Close();
            this.Close();
            Application.Restart(); // O simplemente cierra y vuelve a mostrar login
        }

        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            _client?.Close();
            base.OnFormClosing(e);
        }

        private void cmbConversion_SelectedIndexChanged(object sender, EventArgs e)
        {
            // Opcional: convertir automáticamente al cambiar selección
            btnConvertir_Click(null, null);
        }
    }
}