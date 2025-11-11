using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace WS_EUREKA.AccesoDB
{
    public class AccesoDB
    {
        public SqlConnection GetConnection()
        {
            try
            {
                // Configura la cadena de conexión desde un archivo de configuración
                string connectionString = "Server=localhost; Database=EUREKABANK; User Id=sa; Password=Adminadmin123@;";

                // Devuelve una conexión lista para usar (sin abrir)
                return new SqlConnection(connectionString);
            }
            catch (Exception ex)
            {
                // Lanza una excepción específica para manejo en niveles superiores
                throw new Exception("Error al obtener la conexión a la base de datos: " + ex.Message, ex);
            }
        }
    }
}