using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WS_EUREKA_CLICON.Model;

namespace WS_EUREKA_CLICON.Controller
{
    public class EurekaController
    {
        private readonly EurekaService.EurekaServiceDBSoapClient _client;

        public EurekaController()
        {
            // Instancia del cliente generado por la referencia al servicio
            _client = new EurekaService.EurekaServiceDBSoapClient();
        }

        public List<Movimiento> ObtenerMovimientos(string cuenta)
        {
            var movimientos = new List<Movimiento>();

            try
            {
                // Llamar al método del servicio
                var dataset = _client.GetMovimientoCuenta(cuenta);

                // Convertir DataSet a Lista de Movimiento
                foreach (System.Data.DataRow row in dataset.Tables[0].Rows)
                {
                    movimientos.Add(new Movimiento
                    {
                        Cuenta = row["Cuenta"].ToString(),
                        NroMov = Convert.ToInt32(row["NroMov"]),
                        Fecha = Convert.ToDateTime(row["Fecha"]),
                        Tipo = row["Tipo"].ToString(),
                        Accion = row["Accion"].ToString(),
                        Importe = Convert.ToDouble(row["Importe"])
                    });
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al obtener movimientos: {ex.Message}");
            }

            return movimientos;
        }

        public bool RegistrarDeposito(string cuenta, double importe)
        {
            try
            {
                return _client.RegistrarDeposito(cuenta, importe);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar depósito: {ex.Message}");
                return false;
            }
        }

        public bool RegistrarTransferencia(string cuenta, double importe)
        {
            try
            {
                return _client.RegistrarTransferencia(cuenta, importe);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar depósito: {ex.Message}");
                return false;
            }
        }

        public bool RegistrarRetiro(string cuenta, double importe)
        {
            try
            {
                return _client.RegistrarRetiro(cuenta, importe);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar depósito: {ex.Message}");
                return false;
            }
        }

        public bool ValidarUsuario(string usuario, string password)
        {
            try
            {
                return _client.ValidarUsuario(usuario, password);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al validar usuario: {ex.Message}");
                return false;
            }
        }
    }
}
