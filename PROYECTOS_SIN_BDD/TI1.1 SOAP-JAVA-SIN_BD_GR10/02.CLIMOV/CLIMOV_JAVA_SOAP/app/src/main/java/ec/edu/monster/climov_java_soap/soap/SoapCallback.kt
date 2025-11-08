package ec.edu.monster.climov_java_soap.soap

interface SoapCallback {
    fun onSuccess(result: String)
    fun onError(error: String)
}