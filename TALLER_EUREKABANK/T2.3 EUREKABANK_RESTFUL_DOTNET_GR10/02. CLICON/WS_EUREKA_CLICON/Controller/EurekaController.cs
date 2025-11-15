using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using WS_EUREKA_CLICON.Model;

namespace WS_EUREKA_CLICON.Controller
{
    public class EurekaController
    {
        private readonly HttpClient _httpClient;
        private const string BaseUrl = "http://localhost:5006/api/movimientos";

        public EurekaController()
        {
            _httpClient = new HttpClient();
            _httpClient.DefaultRequestHeaders.Accept.Clear();
            _httpClient.DefaultRequestHeaders.Accept.Add(new System.Net.Http.Headers.MediaTypeWithQualityHeaderValue("application/json"));
        }

        // Método síncrono: ValidarUsuario
        public bool ValidarUsuario(string usuario, string password)
        {
            try
            {
                var request = new { usuario = usuario, password = password };
                var json = JsonSerializer.Serialize(request);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = _httpClient.PostAsync($"{BaseUrl}/autenticar", content).Result;
                if (response.IsSuccessStatusCode)
                {
                    var result = response.Content.ReadAsStringAsync().Result;
                    return bool.Parse(result);
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        // Método síncrono: ObtenerMovimientos
        public List<Movimiento> ObtenerMovimientos(string cuenta)
        {
            try
            {
                var response = _httpClient.GetAsync($"{BaseUrl}/{cuenta}").Result;
                if (response.IsSuccessStatusCode)
                {
                    var json = response.Content.ReadAsStringAsync().Result;
                    return JsonSerializer.Deserialize<List<Movimiento>>(json) ?? new List<Movimiento>();
                }
                return new List<Movimiento>();
            }
            catch
            {
                return new List<Movimiento>();
            }
        }

        // Método síncrono: RegistrarDeposito
        public bool RegistrarDeposito(string cuenta, double importe)
        {
            try
            {
                // Código de empleado fijo (ajusta si necesitas pedirlo)
                string codEmp = "E001";

                var request = new { cuenta = cuenta, importe = importe, codEmp = codEmp };
                var json = JsonSerializer.Serialize(request);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = _httpClient.PostAsync($"{BaseUrl}/deposito", content).Result;
                if (response.IsSuccessStatusCode)
                {
                    var result = response.Content.ReadAsStringAsync().Result;
                    return int.TryParse(result, out int retorno) && retorno == 1;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public bool RegistrarRetiro(string cuenta, double importe)
        {
            try
            {
                string codEmp = "E001";

                var request = new { cuenta = cuenta, importe = importe, codEmp = codEmp };
                var json = JsonSerializer.Serialize(request);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = _httpClient.PostAsync($"{BaseUrl}/retiro", content).Result;
                if (response.IsSuccessStatusCode)
                {
                    var result = response.Content.ReadAsStringAsync().Result;
                    return int.TryParse(result, out int retorno) && retorno == 1;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public bool RegistrarTransferencia(string cuenta, double importe)
        {
            try
            {
                string codEmp = "E001";

                var request = new { cuenta = cuenta, importe = importe, codEmp = codEmp };
                var json = JsonSerializer.Serialize(request);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = _httpClient.PostAsync($"{BaseUrl}/transferencia", content).Result;
                if (response.IsSuccessStatusCode)
                {
                    var result = response.Content.ReadAsStringAsync().Result;
                    return int.TryParse(result, out int retorno) && retorno == 1;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }
    }
}