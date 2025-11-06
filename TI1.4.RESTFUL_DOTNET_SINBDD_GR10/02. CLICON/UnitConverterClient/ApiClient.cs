using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace UnitConverterClient
{
    public class ApiClient
    {
        private readonly HttpClient _httpClient;

        public ApiClient(HttpClient httpClient)
        {
            _httpClient = httpClient;
            // Usar el puerto correcto: 7258
            _httpClient.BaseAddress = new Uri("https://localhost:7258/api/");
        }

        public async Task<T> PostAsync<T>(string endpoint, double value)
        {
            try
            {
                var json = JsonSerializer.Serialize(value);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await _httpClient.PostAsync(endpoint, content);

                if (response.IsSuccessStatusCode)
                {
                    var responseContent = await response.Content.ReadAsStringAsync();
                    var apiResponse = JsonSerializer.Deserialize<ApiResponse<T>>(
                        responseContent,
                        new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                    );

                    if (apiResponse?.Success == true)
                    {
                        return apiResponse.Data;
                    }
                    else
                    {
                        throw new Exception($"Error del servidor: {apiResponse?.Message}");
                    }
                }
                else
                {
                    throw new Exception($"Error HTTP: {response.StatusCode}");
                }
            }
            catch (HttpRequestException ex)
            {
                throw new Exception($"Error de conexión: {ex.Message}");
            }
        }

        public async Task<bool> HealthCheckAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync("Conversion/health");
                return response.IsSuccessStatusCode;
            }
            catch
            {
                return false;
            }
        }
    }

    public class ApiResponse<T>
    {
        public bool Success { get; set; }
        public string Message { get; set; } = string.Empty;
        public T Data { get; set; }
        public DateTime Timestamp { get; set; }
    }
}