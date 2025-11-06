package ec.edu.monster.climov_java_soap.soap

import ec.edu.monster.climov_java_soap.models.ServerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SoapClient {
    companion object {
        private const val CONTENT_TYPE = "text/xml; charset=utf-8"
        private const val SOAP_ACTION = "" // Cambia si tu servicio lo requiere
    }

    suspend fun callWithFailover(soapEnvelope: String, callback: SoapCallback) = withContext(Dispatchers.IO) {
        var lastErr: String? = null
        for (ip in ServerConfig.SERVER_IPS) {
            try {
                val resp = call(ServerConfig.endpoint(ip), soapEnvelope)
                withContext(Dispatchers.Main) { callback.onSuccess(resp) }
                return@withContext
            } catch (e: Exception) {
                lastErr = e.message ?: "Error desconocido"
                // Probar siguiente IP
            }
        }
        withContext(Dispatchers.Main) {
            callback.onError("Error de conexión. Verifique el servidor. Detalle: $lastErr")
        }
    }

    private fun call(endpointUrl: String, body: String): String {
        val url = URL(endpointUrl)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doInput = true
            doOutput = true
            connectTimeout = ServerConfig.CONNECTION_TIMEOUT
            readTimeout = ServerConfig.READ_TIMEOUT
            setRequestProperty("Content-Type", CONTENT_TYPE)
            setRequestProperty("SOAPAction", SOAP_ACTION)
        }

        OutputStreamWriter(conn.outputStream).use { it.write(body); it.flush() }

        val code = conn.responseCode
        if (code != HttpURLConnection.HTTP_OK) {
            conn.disconnect()
            throw RuntimeException("HTTP $code")
        }

        val sb = StringBuilder()
        BufferedReader(InputStreamReader(conn.inputStream)).use { r ->
            var line: String?
            while (r.readLine().also { line = it } != null) sb.append(line)
        }
        conn.disconnect()
        return sb.toString()
    }

    fun extractReturn(xml: String): String? {
        val start = xml.indexOf("<return>")
        val end = xml.indexOf("</return>")
        if (start != -1 && end != -1 && end > start) {
            return xml.substring(start + "<return>".length, end).trim()
        }
        return null
    }
}