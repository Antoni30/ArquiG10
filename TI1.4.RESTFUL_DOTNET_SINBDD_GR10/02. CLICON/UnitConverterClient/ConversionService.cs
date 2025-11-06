using System;
using System.Threading.Tasks;

namespace UnitConverterClient
{
    public class ConversionService
    {
        private readonly ApiClient _apiClient;

        public ConversionService(ApiClient apiClient)
        {
            _apiClient = apiClient;
        }

        public async Task<double> CentimetrosAMetros(double centimetros)
        {
            return await _apiClient.PostAsync<double>("Conversion/centimetros-a-metros", centimetros);
        }

        public async Task<double> MetrosACentimetros(double metros)
        {
            return await _apiClient.PostAsync<double>("Conversion/metros-a-centimetros", metros);
        }

        public async Task<double> MetrosAKilometros(double metros)
        {
            return await _apiClient.PostAsync<double>("Conversion/metros-a-kilometros", metros);
        }

        public async Task<double> ToneladaALibra(double tonelada)
        {
            return await _apiClient.PostAsync<double>("Conversion/tonelada-a-libra", tonelada);
        }

        public async Task<double> KilogramoALibra(double kilogramo)
        {
            return await _apiClient.PostAsync<double>("Conversion/kilogramo-a-libra", kilogramo);
        }

        public async Task<double> GramoAKilogramo(double gramos)
        {
            return await _apiClient.PostAsync<double>("Conversion/gramo-a-kilogramo", gramos);
        }

        public async Task<double> CelsiusAFahrenheit(double celsius)
        {
            return await _apiClient.PostAsync<double>("Conversion/celsius-a-fahrenheit", celsius);
        }

        public async Task<double> FahrenheitACelsius(double fahrenheit)
        {
            return await _apiClient.PostAsync<double>("Conversion/fahrenheit-a-celsius", fahrenheit);
        }

        public async Task<double> CelsiusAKelvin(double celsius)
        {
            return await _apiClient.PostAsync<double>("Conversion/celsius-a-kelvin", celsius);
        }
    }
}