package ec.edu.monster.climov_java_soap.soap

import ec.edu.monster.climov_java_soap.models.ServerConfig

object SoapRequest {
    fun buildEnvelope(methodName: String, paramName: String, value: String): String {
        return """
           <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                             xmlns:ws="${ServerConfig.TARGET_NAMESPACE}">
               <soapenv:Header/>
               <soapenv:Body>
                   <ws:$methodName>
                       <$paramName>$value</$paramName>
                   </ws:$methodName>
               </soapenv:Body>
           </soapenv:Envelope>
        """.trimIndent()
    }
}