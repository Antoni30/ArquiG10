using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WS_EUREKA_CLICON.Model;

namespace WS_EUREKA_CLICON.View
{
    public class EurekaView
    {
        public void MostrarMovimientos(List<Movimiento> movimientos)
        {
            // Mostrar cabecera de la tabla
            Console.WriteLine("+------------+---------------------+---------------------+-----------------+");
            Console.WriteLine("| Nro Mov    | Fecha               | Tipo                | Importe         |");
            Console.WriteLine("+------------+---------------------+---------------------+-----------------+");

            // Mostrar los movimientos en formato tabla
            foreach (var movimiento in movimientos)
            {
                Console.WriteLine("| {0,-10} | {1,-19} | {2,-19} | {3,15:N2} |",
                    movimiento.NroMov,
                    movimiento.Fecha.ToString("yyyy-MM-dd HH:mm:ss"),
                    movimiento.Tipo,
                    movimiento.Importe);
            }

            Console.WriteLine("+------------+---------------------+---------------------+-----------------+");
        }

        public void MostrarResultadoDeposito(bool exito)
        {
            Console.WriteLine(exito ? "Depósito registrado exitosamente." : "Error al registrar el depósito.");
        }

        public void MostrarResultadoTransferencia(bool exito)
        {
            Console.WriteLine(exito ? "Transferencia registrada exitosamente." : "Error al registrar la transferencia.");
        }

        public void MostrarResultadoRetiro(bool exito)
        {
            Console.WriteLine(exito ? "Retiro registrado exitosamente." : "Error al registrar el retiro.");
        }

        public void MostrarValidarUsuario(bool validar)
        {
            Console.WriteLine(validar ? "Ingreso correcto." : "No se pudo validar el usuario.");
        }
    }
}
