package ec.edu.monster.climov_dotnet_soap.models

object ServerConfig {
    val SERVER_IPS = listOf(
        "10.40.3.225",
        "10.40.3.225",
        "10.40.3.225",
        "10.40.3.225"
    )
    const val PORT = "58535"
    const val SERVICE_PATH = "ConversionService.svc"
    const val CONNECTION_TIMEOUT = 5000
    const val READ_TIMEOUT = 10000

    fun endpoint(ip: String) = "http://$ip:$PORT/$SERVICE_PATH"
}