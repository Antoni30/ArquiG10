package ec.edu.monster.climov_dotnet_soap.api

import ec.edu.monster.climov_dotnet_soap.models.ServerConfig
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
        private const val NAMESPACE = "http://tempuri.org/"
        private const val SERVICE_INTERFACE = "IConversionService"
    }

    suspend fun callWithFailover(methodName: String, paramValue: Double, callback: SoapCallback) =
        withContext(Dispatchers.IO) {
            var lastErr: String? = null
            for (ip in ServerConfig.SERVER_IPS) {
                try {
                    val resp = call(ServerConfig.endpoint(ip), buildSoapEnvelope(methodName, paramValue))
                    withContext(Dispatchers.Main) { callback.onSuccess(resp) }
                    return@withContext
                } catch (e: Exception) {
                    lastErr = e.message ?: "Error desconocido"
                }
            }
            withContext(Dispatchers.Main) {
                callback.onError("No se pudo conectar a ninguna IP. Detalle: $lastErr")
            }
        }

    private fun buildSoapEnvelope(methodName: String, value: Double): String {
        val paramName = when (methodName) {
            "CentimetrosAMetros" -> "centimetros"
            "MetrosACentimetros" -> "metros"
            "MetrosAKilometros" -> "metros"
            "ToneladaALibra" -> "tonelada"
            "KilogramoALibra" -> "kilogramo"
            "GramoAKilogramo" -> "gramos"
            "CelsiusAFahrenheit" -> "celsius"
            "FahrenheitACelsius" -> "fahrenheit"
            "CelsiusAKelvin" -> "celsius"
            else -> "value"
        }
        return """
            <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
                <s:Body>
                    <$methodName xmlns="$NAMESPACE">
                        <$paramName>$value</$paramName>
                    </$methodName>
                </s:Body>
            </s:Envelope>
        """.trimIndent()
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
            setRequestProperty("SOAPAction", "\"$NAMESPACE$SERVICE_INTERFACE/${methodNameFromBody(body)}\"")
        }

        OutputStreamWriter(conn.outputStream).use { it.write(body); it.flush() }

        if (conn.responseCode != HttpURLConnection.HTTP_OK) {
            conn.disconnect()
            throw RuntimeException("HTTP ${conn.responseCode}")
        }

        val sb = StringBuilder()
        BufferedReader(InputStreamReader(conn.inputStream)).use { r ->
            var line: String?
            while (r.readLine().also { line = it } != null) sb.append(line)
        }
        conn.disconnect()
        return sb.toString()
    }

    private fun methodNameFromBody(body: String): String {
        val start = body.indexOf('<')
        val end = body.indexOf(' ', start)
        return if (end > start) body.substring(start + 1, end) else "Unknown"
    }

    fun extractResult(xml: String, methodName: String): String? {
        val startTag = "<${methodName}Result>"
        val start = xml.indexOf(startTag)
        if (start == -1) return null
        val end = xml.indexOf("</", start + startTag.length)
        if (end == -1) return null
        return xml.substring(start + startTag.length, end).trim()
    }
}

interface SoapCallback {
    fun onSuccess(xmlResponse: String)
    fun onError(error: String)
}