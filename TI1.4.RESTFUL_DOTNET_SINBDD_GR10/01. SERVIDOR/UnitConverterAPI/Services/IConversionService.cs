namespace UnitConverterAPI.Services
{
    public interface IConversionService
    {
        double CentimetrosAMetros(double centimetros);
        double MetrosACentimetros(double metros);
        double MetrosAKilometros(double metros);
        double ToneladaALibra(double tonelada);
        double KilogramoALibra(double kilogramo);
        double GramoAKilogramo(double gramos);
        double CelsiusAFahrenheit(double celsius);
        double FahrenheitACelsius(double fahrenheit);
        double CelsiusAKelvin(double celsius);
    }
}