using Microsoft.AspNetCore.Mvc;
using WS_EUREKA.Models;
using WS_EUREKA.Services;

namespace WS_EUREKA.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class MovimientosController : ControllerBase
    {
        private readonly EurekaService _service = new EurekaService();

        // GET: api/movimientos/{cuenta}
        [HttpGet("{cuenta}")]
        public ActionResult<List<Movimiento>> ObtenerMovimientos(string cuenta)
        {
            try
            {
                var movimientos = _service.LeerMovimientos(cuenta);
                return Ok(movimientos);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Error al obtener movimientos", detalle = ex.Message });
            }
        }

        // POST: api/movimientos/deposito
        [HttpPost("deposito")]
        public ActionResult<int> RegistrarDeposito([FromBody] DepositoRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Cuenta))
                return BadRequest("Datos incompletos");

            try
            {
                int resultado = _service.RegistrarDeposito(request.Cuenta, request.Importe, request.CodEmp);
                return Ok(resultado);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Error al registrar depósito", detalle = ex.Message });
            }
        }

        // POST: api/movimientos/retiro
        [HttpPost("retiro")]
        public ActionResult<int> RegistrarRetiro([FromBody] RetiroRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Cuenta))
                return BadRequest("Datos incompletos");

            try
            {
                int resultado = _service.RegistrarRetiro(request.Cuenta, request.Importe, request.CodEmp);
                return Ok(resultado);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Error al registrar retiro", detalle = ex.Message });
            }
        }

        // POST: api/movimientos/transferencia
        [HttpPost("transferencia")]
        public ActionResult<int> RegistrarTransferencia([FromBody] TransferenciaRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Cuenta))
                return BadRequest("Datos incompletos");

            try
            {
                int resultado = _service.RegistrarTransferencia(request.Cuenta, request.Importe, request.CodEmp);
                return Ok(resultado);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Error al registrar transferencia", detalle = ex.Message });
            }
        }

        // POST: api/movimientos/autenticar
        [HttpPost("autenticar")]
        public ActionResult<bool> Autenticar([FromBody] LoginRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Usuario) || string.IsNullOrWhiteSpace(request.Password))
                return BadRequest("Usuario y contraseña son requeridos.");

            try
            {
                bool esValido = _service.ValidarUsuario(request.Usuario, request.Password);
                return Ok(esValido);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Error al validar credenciales", detalle = ex.Message });
            }
        }
    }

    // DTOs para las solicitudes
    public class DepositoRequest
    {
        public string Cuenta { get; set; } = string.Empty;
        public double Importe { get; set; }
        public string CodEmp { get; set; } = string.Empty;
    }

    public class RetiroRequest
    {
        public string Cuenta { get; set; } = string.Empty;
        public double Importe { get; set; }
        public string CodEmp { get; set; } = string.Empty;
    }

    public class TransferenciaRequest
    {
        public string Cuenta { get; set; } = string.Empty;
        public double Importe { get; set; }
        public string CodEmp { get; set; } = string.Empty;
    }

    public class LoginRequest
    {
        public string Usuario { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
    }
}