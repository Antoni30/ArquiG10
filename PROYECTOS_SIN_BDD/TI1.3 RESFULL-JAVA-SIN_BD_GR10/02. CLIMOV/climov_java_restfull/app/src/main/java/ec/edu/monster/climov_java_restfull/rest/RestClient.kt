package ec.edu.monster.climov_java_restfull.rest

import ec.edu.monster.climov_java_restfull.models.ServerConfig
import ec.edu.monster.climov_java_restfull.api.ApiService
import ec.edu.monster.climov_java_restfull.models.Entrada
import ec.edu.monster.climov_java_restfull.models.Resultado
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RestClient {
    suspend fun callWithFailover(entrada: Entrada, callback: RestCallback) {
        var lastErr: String? = null
        for (ip in ServerConfig.SERVER_IPS) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(ServerConfig.CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                    .readTimeout(ServerConfig.READ_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(ServerConfig.baseUrl(ip))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val response = service.convertirUnidad(entrada)

                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                    return
                } else {
                    lastErr = "HTTP ${response.code()}"
                }
            } catch (e: IOException) {
                lastErr = e.message ?: "Error de red"
            } catch (e: Exception) {
                lastErr = e.message ?: "Error interno"
            }
        }
        callback.onError("Error de conexión. Verifique el servidor. Detalle: $lastErr")
    }
}

interface RestCallback {
    fun onSuccess(result: Resultado)
    fun onError(error: String)
}