namespace UnitConverterAPI.Services
{
    public class ConversionService : IConversionService
    {
        public double CentimetrosAMetros(double centimetros)
        {
            return centimetros / 100.0;
        }

        public double MetrosACentimetros(double metros)
        {
            return metros * 100.0;
        }

        public double MetrosAKilometros(double metros)
        {
            return metros / 1000.0;
        }

        public double ToneladaALibra(double tonelada)
        {
            return tonelada * 2204.62;
        }

        public double KilogramoALibra(double kilogramo)
        {
            return kilogramo * 2.20462;
        }

        public double GramoAKilogramo(double gramos)
        {
            return gramos / 1000.0;
        }

        public double CelsiusAFahrenheit(double celsius)
        {
            return (celsius * 9 / 5) + 32;
        }

        public double FahrenheitACelsius(double fahrenheit)
        {
            return (fahrenheit - 32) * 5 / 9;
        }

        public double CelsiusAKelvin(double celsius)
        {
            return celsius + 273.15;
        }
    }
}