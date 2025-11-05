package ec.edu.monster.climov_java_restfull.models

object ServerConfig {
    val SERVER_IPS = listOf(
        "10.40.21.0",
        "192.168.18.7",
        "192.168.18.9",
        "192.168.18.22"
    )
    const val PORT = "8080"
    const val APP_CONTEXT = "ConUni_RESTFULL_Java_G10"

    const val CONNECTION_TIMEOUT = 5000
    const val READ_TIMEOUT = 10000

    fun baseUrl(ip: String) = "http://$ip:$PORT/$APP_CONTEXT/"
}