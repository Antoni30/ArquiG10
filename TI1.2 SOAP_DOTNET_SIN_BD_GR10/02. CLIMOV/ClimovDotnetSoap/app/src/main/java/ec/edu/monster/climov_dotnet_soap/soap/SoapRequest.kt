package ec.edu.monster.climov_dotnet_soap.soap

object SoapRequest {
    private const val NAMESPACE = "http://tempuri.org/"

    fun buildEnvelope(methodName: String, value: Double): String {
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
}