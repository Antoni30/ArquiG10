using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using WS_EUREKA.Model;
using WS_EUREKA.AccesoDB;

namespace WS_EUREKA.EurekaService
{
    public class EurekaService
    {
        private readonly WS_EUREKA.AccesoDB.AccesoDB accesoDB = new WS_EUREKA.AccesoDB.AccesoDB();

        public List<Movimiento> LeerMovimientos(string cuenta)
        {
            var lista = new List<Movimiento>();
            string sql = @"
                SELECT 
                    m.chr_cuencodigo AS cuenta,
                    m.int_movinumero AS nromov,
                    m.dtt_movifecha AS fecha,
                    t.vch_tipodescripcion AS tipo,
                    t.vch_tipoaccion AS accion,
                    m.dec_moviimporte AS importe
                FROM tipomovimiento t
                INNER JOIN movimiento m ON t.chr_tipocodigo = m.chr_tipocodigo
                WHERE m.chr_cuencodigo = @cuenta";

            using (var cn = accesoDB.GetConnection())
            {
                try
                {
                    cn.Open(); // Abrimos la conexión

                    using (SqlCommand cmd = new SqlCommand(sql, cn))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                var movimiento = new Movimiento
                                {
                                    Cuenta = reader["cuenta"].ToString(),
                                    NroMov = Convert.ToInt32(reader["nromov"]),
                                    Fecha = Convert.ToDateTime(reader["fecha"]),
                                    Tipo = reader["tipo"].ToString(),
                                    Accion = reader["accion"].ToString(),
                                    Importe = Convert.ToDouble(reader["importe"])
                                };
                                lista.Add(movimiento);
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    throw new Exception("Error al leer movimientos: " + ex.Message, ex);
                }
            }

            return lista;
        }

        public int RegistrarDeposito(string cuenta, double importe, string codEmp)
        {
            string sql1 = @"
                SELECT dec_cuensaldo, int_cuencontmov
                FROM cuenta
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql2 = @"
                UPDATE cuenta
                SET dec_cuensaldo = @saldo,
                    int_cuencontmov = @cont
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql3 = @"
                INSERT INTO movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte)
                VALUES(@cuenta, @cont, GETDATE(), @codEmp, '003', @importe)";

            using (var cn = accesoDB.GetConnection())
            {
                cn.Open();
                SqlTransaction transaction = cn.BeginTransaction(System.Data.IsolationLevel.Serializable);

                try
                {
                    double saldo;
                    int cont;

                    // Paso 1: Leer datos de la cuenta con un bloqueo compartido
                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1; // La cuenta no existe o no está activa
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    // Paso 2: Actualizar la cuenta
                    saldo += importe;
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        cmd.ExecuteNonQuery();
                    }

                    // Paso 3: Registrar el movimiento
                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);

                        cmd.ExecuteNonQuery();
                    }

                    // Confirmar la transacción
                    transaction.Commit();
                    return 1; // Éxito
                }
                catch (Exception ex)
                {
                    // Rollback en caso de error
                    transaction.Rollback();
                    throw new Exception("Error al registrar el depósito: " + ex.Message, ex);
                }
            }
        }

        public int RegistrarRetiro(string cuenta, double importe, string codEmp)
        {
            string sql1 = @"
                SELECT dec_cuensaldo, int_cuencontmov
                FROM cuenta
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql2 = @"
                UPDATE cuenta
                SET dec_cuensaldo = @saldo,
                    int_cuencontmov = @cont
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql3 = @"
                INSERT INTO movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte)
                VALUES(@cuenta, @cont, GETDATE(), @codEmp, '004', @importe)";

            using (var cn = accesoDB.GetConnection())
            {
                cn.Open();
                SqlTransaction transaction = cn.BeginTransaction(System.Data.IsolationLevel.Serializable);

                try
                {
                    double saldo;
                    int cont;

                    // Paso 1: Leer datos de la cuenta con un bloqueo compartido
                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1; // La cuenta no existe o no está activa
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    // Paso 2: Actualizar la cuenta
                    saldo += importe;
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        cmd.ExecuteNonQuery();
                    }

                    // Paso 3: Registrar el movimiento
                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);

                        cmd.ExecuteNonQuery();
                    }

                    // Confirmar la transacción
                    transaction.Commit();
                    return 1; // Éxito
                }
                catch (Exception ex)
                {
                    // Rollback en caso de error
                    transaction.Rollback();
                    throw new Exception("Error al registrar el depósito: " + ex.Message, ex);
                }
            }
        }

        public int RegistrarTransferencia(string cuenta, double importe, string codEmp)
        {
            string sql1 = @"
                SELECT dec_cuensaldo, int_cuencontmov
                FROM cuenta
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql2 = @"
                UPDATE cuenta
                SET dec_cuensaldo = @saldo,
                    int_cuencontmov = @cont
                WHERE chr_cuencodigo = @cuenta AND vch_cuenestado = 'ACTIVO'";

            string sql3 = @"
                INSERT INTO movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte)
                VALUES(@cuenta, @cont, GETDATE(), @codEmp, '008', @importe)";

            using (var cn = accesoDB.GetConnection())
            {
                cn.Open();
                SqlTransaction transaction = cn.BeginTransaction(System.Data.IsolationLevel.Serializable);

                try
                {
                    double saldo;
                    int cont;

                    // Paso 1: Leer datos de la cuenta con un bloqueo compartido
                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1; // La cuenta no existe o no está activa
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    // Paso 2: Actualizar la cuenta
                    saldo += importe;
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        cmd.ExecuteNonQuery();
                    }

                    // Paso 3: Registrar el movimiento
                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);

                        cmd.ExecuteNonQuery();
                    }

                    // Confirmar la transacción
                    transaction.Commit();
                    return 1; // Éxito
                }
                catch (Exception ex)
                {
                    // Rollback en caso de error
                    transaction.Rollback();
                    throw new Exception("Error al registrar el depósito: " + ex.Message, ex);
                }
            }
        }

        public bool ValidarUsuario(string usuario, string password)
        {
            string sql = @"
            SELECT PASSWORD 
            FROM auth 
            WHERE USUARIO = @usuario";

            using (var cn = accesoDB.GetConnection())
            {
                try
                {
                    cn.Open(); // Abrimos la conexión

                    using (SqlCommand cmd = new SqlCommand(sql, cn))
                    {
                        // Añadimos los parámetros para evitar inyección SQL
                        cmd.Parameters.AddWithValue("@usuario", usuario);

                        // Ejecutamos el comando para obtener la contraseña almacenada
                        object result = cmd.ExecuteScalar();

                        if (result == null)
                        {
                            Console.WriteLine($"Usuario '{usuario}' no encontrado en la base de datos.");
                            return false;
                        }

                        // Convertimos el resultado a byte[] y luego a cadena hexadecimal
                        byte[] dbPasswordBytes = (byte[])result;
                        string dbPasswordHash = ConvertirBytesAHexadecimal(dbPasswordBytes);

                        // Generamos el hash de la contraseña proporcionada
                        string inputPasswordHash = GenerarHashSHA256(password);

                        // Depuración: imprimir hash generado y almacenado
                        System.Diagnostics.Debug.WriteLine($"Hash proporcionado: {inputPasswordHash}");
                        System.Diagnostics.Debug.WriteLine($"Hash almacenado: {dbPasswordHash}");

                        // Comparamos el hash proporcionado con el almacenado
                        bool sonIguales = inputPasswordHash.Equals(dbPasswordHash, StringComparison.OrdinalIgnoreCase);

                        // Resultado
                        System.Diagnostics.Debug.WriteLine(sonIguales
                            ? "Las credenciales coinciden."
                            : "Las credenciales no coinciden.");

                        return sonIguales;
                    }
                }
                catch (Exception ex)
                {
                    // Manejo de excepciones
                    throw new Exception("Error al validar el usuario: " + ex.Message, ex);
                }
            }
        }

        /// <summary>
        /// Convierte un arreglo de bytes a una cadena hexadecimal.
        /// </summary>
        /// <param name="bytes">El arreglo de bytes a convertir.</param>
        /// <returns>Una representación en cadena hexadecimal de los bytes.</returns>
        private string ConvertirBytesAHexadecimal(byte[] bytes)
        {
            return BitConverter.ToString(bytes).Replace("-", "").ToUpper();
        }

        /// <summary>
        /// Genera un hash SHA-256 para una contraseña dada.
        /// </summary>
        /// <param name="input">La contraseña en texto plano.</param>
        /// <returns>El hash SHA-256 de la contraseña.</returns>
        private string GenerarHashSHA256(string input)
        {
            using (var sha256 = System.Security.Cryptography.SHA256.Create())
            {
                byte[] bytes = sha256.ComputeHash(System.Text.Encoding.UTF8.GetBytes(input));
                return BitConverter.ToString(bytes).Replace("-", "").ToUpper();
            }
        }

    }
}