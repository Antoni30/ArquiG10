using System.ServiceModel;

namespace ec.edu.monster.Contracts
{
    [ServiceContract]
    public interface IConversionService
    {
        [OperationContract]
        double CentimetrosAMetros(double centimetros);

        [OperationContract]
        double MetrosACentimetros(double metros);

        [OperationContract]
        double MetrosAKilometros(double metros);

        [OperationContract]
        double ToneladaALibra(double tonelada);

        [OperationContract]
        double KilogramoALibra(double kilogramo);

        [OperationContract]
        double GramoAKilogramo(double gramos);

        [OperationContract]
        double CelsiusAFahrenheit(double celsius);

        [OperationContract]
        double FahrenheitACelsius(double fahrenheit);

        [OperationContract]
        double CelsiusAKelvin(double celsius);
    }
}