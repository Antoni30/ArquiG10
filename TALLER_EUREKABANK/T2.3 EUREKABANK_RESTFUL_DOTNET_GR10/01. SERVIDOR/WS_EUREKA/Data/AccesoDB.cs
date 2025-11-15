using System;
using Microsoft.Data.SqlClient; // Este using requiere el paquete NuGet mencionado.

namespace WS_EUREKA.Data
{
    public class AccesoDB
    {
        public SqlConnection GetConnection()
        {
            try
            {
                // Cadena de conexión directa (como en tu versión original)
                string connectionString = "Server=localhost; Database=EUREKABANK; User Id=sa; Password=Adminadmin123@; TrustServerCertificate=true;";
                return new SqlConnection(connectionString);
            }
            catch (Exception ex)
            {
                throw new Exception("Error al obtener la conexión a la base de datos: " + ex.Message, ex);
            }
        }
    }
}