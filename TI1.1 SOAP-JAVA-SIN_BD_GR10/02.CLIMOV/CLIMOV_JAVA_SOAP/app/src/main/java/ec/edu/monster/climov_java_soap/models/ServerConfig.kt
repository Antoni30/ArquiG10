package ec.edu.monster.climov_java_soap.models

object ServerConfig {
    // Reemplaza con tus IPs reales (orden de failover)
    val SERVER_IPS = listOf(
        "192.168.56.1",
        "192.168.18.6",
        "192.168.1.102",
        "192.168.1.103"
    )
    const val PORT = "8080"
    const val APP_CONTEXT = "ConUni_Soap_Java_G10"
    const val SERVICE = "WSConversionUnidades"
    const val TARGET_NAMESPACE = "http://ws.moster.edu.ec/"

    const val CONNECTION_TIMEOUT = 5000
    const val READ_TIMEOUT = 10000

    fun endpoint(ip: String) = "http://$ip:$PORT/$APP_CONTEXT/$SERVICE"
}