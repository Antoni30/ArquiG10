using Microsoft.AspNetCore.Mvc;
using UnitConverterAPI.Models;  // ← ESTA LÍNEA FALTABA
using UnitConverterAPI.Services;

namespace UnitConverterAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ConversionController : ControllerBase
    {
        private readonly IConversionService _conversionService;

        public ConversionController(IConversionService conversionService)
        {
            _conversionService = conversionService;
        }

        [HttpPost("centimetros-a-metros")]
        public ActionResult<ApiResponse<double>> CentimetrosAMetros([FromBody] double centimetros)
        {
            try
            {
                var resultado = _conversionService.CentimetrosAMetros(centimetros);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de centímetros a metros",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("metros-a-centimetros")]
        public ActionResult<ApiResponse<double>> MetrosACentimetros([FromBody] double metros)
        {
            try
            {
                var resultado = _conversionService.MetrosACentimetros(metros);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de metros a centímetros",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("metros-a-kilometros")]
        public ActionResult<ApiResponse<double>> MetrosAKilometros([FromBody] double metros)
        {
            try
            {
                var resultado = _conversionService.MetrosAKilometros(metros);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de metros a kilómetros",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("tonelada-a-libra")]
        public ActionResult<ApiResponse<double>> ToneladaALibra([FromBody] double tonelada)
        {
            try
            {
                var resultado = _conversionService.ToneladaALibra(tonelada);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de toneladas a libras",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("kilogramo-a-libra")]
        public ActionResult<ApiResponse<double>> KilogramoALibra([FromBody] double kilogramo)
        {
            try
            {
                var resultado = _conversionService.KilogramoALibra(kilogramo);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de kilogramos a libras",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("gramo-a-kilogramo")]
        public ActionResult<ApiResponse<double>> GramoAKilogramo([FromBody] double gramos)
        {
            try
            {
                var resultado = _conversionService.GramoAKilogramo(gramos);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de gramos a kilogramos",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("celsius-a-fahrenheit")]
        public ActionResult<ApiResponse<double>> CelsiusAFahrenheit([FromBody] double celsius)
        {
            try
            {
                var resultado = _conversionService.CelsiusAFahrenheit(celsius);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de Celsius a Fahrenheit",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("fahrenheit-a-celsius")]
        public ActionResult<ApiResponse<double>> FahrenheitACelsius([FromBody] double fahrenheit)
        {
            try
            {
                var resultado = _conversionService.FahrenheitACelsius(fahrenheit);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de Fahrenheit a Celsius",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpPost("celsius-a-kelvin")]
        public ActionResult<ApiResponse<double>> CelsiusAKelvin([FromBody] double celsius)
        {
            try
            {
                var resultado = _conversionService.CelsiusAKelvin(celsius);
                return Ok(new ApiResponse<double>
                {
                    Success = true,
                    Message = "Conversión exitosa de Celsius a Kelvin",
                    Data = resultado
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new ApiResponse<double>
                {
                    Success = false,
                    Message = $"Error en la conversión: {ex.Message}",
                    Data = 0
                });
            }
        }

        [HttpGet("health")]
        public ActionResult<ApiResponse<string>> HealthCheck()
        {
            return Ok(new ApiResponse<string>
            {
                Success = true,
                Message = "API de Conversión está funcionando correctamente",
                Data = "Conversion API v1.0 - Status: OK"
            });
        }

        [HttpGet("funciones")]
        public ActionResult<ApiResponse<List<string>>> GetAvailableFunctions()
        {
            var funciones = new List<string>
            {
                "centimetros-a-metros",
                "metros-a-centimetros",
                "metros-a-kilometros",
                "tonelada-a-libra",
                "kilogramo-a-libra",
                "gramo-a-kilogramo",
                "celsius-a-fahrenheit",
                "fahrenheit-a-celsius",
                "celsius-a-kelvin"
            };

            return Ok(new ApiResponse<List<string>>
            {
                Success = true,
                Message = "Funciones disponibles obtenidas exitosamente",
                Data = funciones
            });
        }
    }
}