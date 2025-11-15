using System;
using System.Collections.Generic;
using Microsoft.Data.SqlClient;
using WS_EUREKA.Models;        // ← Usa la carpeta Models
using WS_EUREKA.Data;

namespace WS_EUREKA.Services
{
    public class EurekaService
    {
        private readonly Data.AccesoDB accesoDB = new Data.AccesoDB();

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
                    cn.Open();

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

                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1;
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    saldo += importe;
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.ExecuteNonQuery();
                    }

                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);
                        cmd.ExecuteNonQuery();
                    }

                    transaction.Commit();
                    return 1;
                }
                catch (Exception ex)
                {
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

                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1;
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    saldo -= importe; // ⚠️ Aquí debe ser resta (corregido respecto a tu código original)
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.ExecuteNonQuery();
                    }

                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);
                        cmd.ExecuteNonQuery();
                    }

                    transaction.Commit();
                    return 1;
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                    throw new Exception("Error al registrar el retiro: " + ex.Message, ex);
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

                    using (SqlCommand cmd = new SqlCommand(sql1, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);

                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (!reader.Read())
                            {
                                return -1;
                            }

                            saldo = Convert.ToDouble(reader["dec_cuensaldo"]);
                            cont = Convert.ToInt32(reader["int_cuencontmov"]);
                        }
                    }

                    saldo -= importe; // ⚠️ En transferencia, también se resta del origen
                    cont++;

                    using (SqlCommand cmd = new SqlCommand(sql2, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@saldo", saldo);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.ExecuteNonQuery();
                    }

                    using (SqlCommand cmd = new SqlCommand(sql3, cn, transaction))
                    {
                        cmd.Parameters.AddWithValue("@cuenta", cuenta);
                        cmd.Parameters.AddWithValue("@cont", cont);
                        cmd.Parameters.AddWithValue("@codEmp", codEmp);
                        cmd.Parameters.AddWithValue("@importe", importe);
                        cmd.ExecuteNonQuery();
                    }

                    transaction.Commit();
                    return 1;
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                    throw new Exception("Error al registrar la transferencia: " + ex.Message, ex);
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
                    cn.Open();

                    using (SqlCommand cmd = new SqlCommand(sql, cn))
                    {
                        cmd.Parameters.AddWithValue("@usuario", usuario);
                        object result = cmd.ExecuteScalar();

                        if (result == null)
                        {
                            return false;
                        }

                        byte[] dbPasswordBytes = (byte[])result;
                        string dbPasswordHash = ConvertirBytesAHexadecimal(dbPasswordBytes);
                        string inputPasswordHash = GenerarHashSHA256(password);

                        return inputPasswordHash.Equals(dbPasswordHash, StringComparison.OrdinalIgnoreCase);
                    }
                }
                catch (Exception ex)
                {
                    throw new Exception("Error al validar el usuario: " + ex.Message, ex);
                }
            }
        }

        private string ConvertirBytesAHexadecimal(byte[] bytes)
        {
            return BitConverter.ToString(bytes).Replace("-", "").ToUpper();
        }

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