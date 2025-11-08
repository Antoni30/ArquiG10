package ec.edu.monster.climov_dotnet_soap.soap

interface SoapCallback {
    fun onSuccess(result: String)
    fun onError(error: String)
}