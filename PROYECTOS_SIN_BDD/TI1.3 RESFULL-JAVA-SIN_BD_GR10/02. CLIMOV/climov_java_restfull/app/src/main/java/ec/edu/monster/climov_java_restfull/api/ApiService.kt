package ec.edu.monster.climov_java_restfull.api

import ec.edu.monster.climov_java_restfull.models.Entrada
import ec.edu.monster.climov_java_restfull.models.Resultado
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("webresources/servicio/conversionUnidades")
    suspend fun convertirUnidad(@Body entrada: Entrada): Response<Resultado>
}