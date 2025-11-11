using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace WS_EUREKA.Service
{
    /// <summary>
    /// Summary description for EurekaServiceDB
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class EurekaServiceDB : System.Web.Services.WebService
    {
        private readonly EurekaService.EurekaService _eurekaService = new EurekaService.EurekaService();

        [WebMethod]
        public DataSet GetMovimientoCuenta(string cuenta)
        {
            try
            {
                // Utilizar la lógica existente en EurekaService para obtener movimientos
                var movimientos = _eurekaService.LeerMovimientos(cuenta);

                // Convertir la lista de movimientos en un DataSet
                var dataSet = new DataSet();
                var dataTable = new DataTable("Movimientos");

                // Crear columnas basadas en las propiedades del objeto Movimiento
                dataTable.Columns.Add("Cuenta", typeof(string));
                dataTable.Columns.Add("NroMov", typeof(int));
                dataTable.Columns.Add("Fecha", typeof(DateTime));
                dataTable.Columns.Add("Tipo", typeof(string));
                dataTable.Columns.Add("Accion", typeof(string));
                dataTable.Columns.Add("Importe", typeof(double));

                // Llenar la tabla con datos
                foreach (var movimiento in movimientos)
                {
                    dataTable.Rows.Add(
                        movimiento.Cuenta,
                        movimiento.NroMov,
                        movimiento.Fecha,
                        movimiento.Tipo,
                        movimiento.Accion,
                        movimiento.Importe
                    );
                }

                dataSet.Tables.Add(dataTable);
                return dataSet;
            }
            catch (Exception ex)
            {
                // Manejo de errores
                throw new Exception("Error al obtener movimientos: " + ex.Message, ex);
            }
        }

        [WebMethod]
        public bool RegistrarDeposito(string cuenta, double importe)
        {
            string codEmp = "0001";

            // Validación de entrada
            if (string.IsNullOrWhiteSpace(cuenta) || importe <= 0)
            {
                // Si los datos son inválidos, devuelve falso inmediatamente
                return false;
            }

            try
            {
                // Invocar el método existente para registrar depósito
                int resultado = _eurekaService.RegistrarDeposito(cuenta, importe, codEmp);

                // Devuelve true si el resultado es 1, false en cualquier otro caso
                return resultado == 1;
            }
            catch (Exception ex)
            {
                // Loguear el error para análisis (puedes usar System.Diagnostics o un logger)
                System.Diagnostics.Debug.WriteLine($"Error en RegistrarDeposito: {ex.Message}");

                // Devuelve falso en caso de error
                return false;
            }
        }

        [WebMethod]
        public bool RegistrarRetiro(string cuenta, double importe)
        {
            string codEmp = "0001";

            // Validación de entrada
            if (string.IsNullOrWhiteSpace(cuenta) || importe <= 0)
            {
                // Si los datos son inválidos, devuelve falso inmediatamente
                return false;
            }

            try
            {
                // Invocar el método existente para registrar depósito
                int resultado = _eurekaService.RegistrarRetiro(cuenta, importe, codEmp);

                // Devuelve true si el resultado es 1, false en cualquier otro caso
                return resultado == 1;
            }
            catch (Exception ex)
            {
                // Loguear el error para análisis (puedes usar System.Diagnostics o un logger)
                System.Diagnostics.Debug.WriteLine($"Error en RegistrarDeposito: {ex.Message}");

                // Devuelve falso en caso de error
                return false;
            }
        }

        [WebMethod]
        public bool RegistrarTransferencia(string cuenta, double importe)
        {
            string codEmp = "0001";

            // Validación de entrada
            if (string.IsNullOrWhiteSpace(cuenta) || importe <= 0)
            {
                // Si los datos son inválidos, devuelve falso inmediatamente
                return false;
            }

            try
            {
                // Invocar el método existente para registrar depósito
                int resultado = _eurekaService.RegistrarTransferencia(cuenta, importe, codEmp);

                // Devuelve true si el resultado es 1, false en cualquier otro caso
                return resultado == 1;
            }
            catch (Exception ex)
            {
                // Loguear el error para análisis (puedes usar System.Diagnostics o un logger)
                System.Diagnostics.Debug.WriteLine($"Error en RegistrarDeposito: {ex.Message}");

                // Devuelve falso en caso de error
                return false;
            }
        }

        [WebMethod]
        public bool ValidarUsuario(string usuario, string password)
        {
            try
            {
                // Validar las credenciales utilizando el servicio interno
                return _eurekaService.ValidarUsuario(usuario, password);
            }
            catch (Exception ex)
            {
                // Loguear el error (opcional) para análisis
                System.Diagnostics.Debug.WriteLine($"Error en ValidarUsuario: {ex.Message}");

                // Retornar falso en caso de error
                return false;
            }
        }

    }
}
