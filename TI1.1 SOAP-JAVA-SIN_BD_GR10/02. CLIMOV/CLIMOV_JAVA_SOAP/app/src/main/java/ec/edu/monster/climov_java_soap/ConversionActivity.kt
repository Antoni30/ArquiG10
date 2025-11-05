package ec.edu.monster.climov_java_soap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ec.edu.monster.climov_java_soap.soap.SoapClient
import ec.edu.monster.climov_java_soap.soap.SoapRequest
import ec.edu.monster.climov_java_soap.utils.SessionManager
import kotlinx.coroutines.launch

class ConversionActivity : AppCompatActivity() {

    private lateinit var valueInput: EditText
    private lateinit var operationSpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var resultText: TextView
    private lateinit var logoutButton: Button

    private lateinit var session: SessionManager
    private val soapClient = SoapClient()

    private enum class Tab { LONGITUD, MASA, TEMPERATURA }
    private var currentTab = Tab.LONGITUD

    private val opsLongitud = listOf(
        Triple("Centímetros a Metros", "centimetrosAMetros", "centimetros"),
        Triple("Metros a Centímetros", "metrosACentimetros", "metros"),
        Triple("Metros a Kilómetros", "metrosAKilometros", "metros")
    )
    private val opsMasa = listOf(
        Triple("Tonelada a Libra", "toneladaALibra", "toneladas"),
        Triple("Kilogramo a Libra", "kilogramoALibra", "kilogramos"),
        Triple("Gramo a Kilogramo", "gramoAKilogramo", "gramos")
    )
    private val opsTemp = listOf(
        Triple("Celsius a Fahrenheit", "celsiusAFahrenheit", "celsius"),
        Triple("Fahrenheit a Celsius", "fahrenheitACelsius", "fahrenheit"),
        Triple("Celsius a Kelvin", "celsiusAKelvin", "celsius")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)

        session = SessionManager(this)
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java)); finish(); return
        }

        valueInput = findViewById(R.id.valueInput)
        operationSpinner = findViewById(R.id.operationSpinner)
        convertButton = findViewById(R.id.convertButton)
        resultText = findViewById(R.id.resultText)
        logoutButton = findViewById(R.id.logoutButton)

        findViewById<Button>(R.id.tabLength).setOnClickListener { setTab(Tab.LONGITUD) }
        findViewById<Button>(R.id.tabMass).setOnClickListener { setTab(Tab.MASA) }
        findViewById<Button>(R.id.tabTemp).setOnClickListener { setTab(Tab.TEMPERATURA) }

        setTab(Tab.LONGITUD)

        convertButton.setOnClickListener { doConvert() }
        logoutButton.setOnClickListener {
            session.setLoggedIn(false)
            startActivity(Intent(this, LoginActivity::class.java)); finish()
        }
    }

    private fun setTab(tab: Tab) {
        currentTab = tab
        resultText.visibility = View.GONE
        val ops = when (tab) {
            Tab.LONGITUD -> opsLongitud
            Tab.MASA -> opsMasa
            Tab.TEMPERATURA -> opsTemp
        }
        val items = ops.map { it.first }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        operationSpinner.adapter = adapter
        operationSpinner.setSelection(0)
    }

    private fun doConvert() {
        resultText.visibility = View.GONE
        val input = valueInput.text.toString().trim()
        val value = input.toDoubleOrNull()
        if (value == null) {
            showMsg(getString(R.string.invalid_input), false); return
        }

        val ops = when (currentTab) {
            Tab.LONGITUD -> opsLongitud
            Tab.MASA -> opsMasa
            Tab.TEMPERATURA -> opsTemp
        }
        val idx = operationSpinner.selectedItemPosition
        if (idx !in ops.indices) {
            showMsg(getString(R.string.invalid_input), false); return
        }

        val methodName = ops[idx].second
        val paramName = ops[idx].third

        convertButton.isEnabled = false
        convertButton.text = getString(R.string.processing)

        val envelope = SoapRequest.buildEnvelope(methodName, paramName, value.toString())

        lifecycleScope.launch {
            soapClient.callWithFailover(envelope, object : ec.edu.monster.climov_java_soap.soap.SoapCallback {
                override fun onSuccess(result: String) {
                    convertButton.isEnabled = true
                    convertButton.text = getString(R.string.convert_button)
                    val ret = soapClient.extractReturn(result)
                    if (ret == null) {
                        showMsg(getString(R.string.conversion_error), false)
                    } else {
                        showMsg("Resultado: $ret", true)
                    }
                }
                override fun onError(error: String) {
                    convertButton.isEnabled = true
                    convertButton.text = getString(R.string.convert_button)
                    showMsg(error, false)
                }
            })
        }
    }

    private fun showMsg(msg: String, success: Boolean) {
        resultText.text = msg
        resultText.visibility = View.VISIBLE
        val bg = if (success) R.color.success_bg else R.color.error_bg
        val fg = if (success) R.color.success_text else R.color.error_text
        resultText.setBackgroundColor(ContextCompat.getColor(this, bg))
        resultText.setTextColor(ContextCompat.getColor(this, fg))
    }
}