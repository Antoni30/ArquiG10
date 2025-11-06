package ec.edu.monster.climov_java_restfull

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ec.edu.monster.climov_java_restfull.models.Entrada
import ec.edu.monster.climov_java_restfull.rest.RestClient
import ec.edu.monster.climov_java_restfull.utils.SessionManager
import kotlinx.coroutines.launch

class ConversionActivity : AppCompatActivity() {

    private lateinit var valueInput: EditText
    private lateinit var operationSpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var resultText: TextView
    private lateinit var logoutButton: Button

    private lateinit var session: SessionManager
    private val restClient = RestClient()

    private enum class Tab { LONGITUD, MASA, TEMPERATURA }
    private var currentTab = Tab.LONGITUD

    private val opsLongitud = listOf(
        "Centímetros a Metros" to "centimetroAmetros",
        "Metros a Centímetros" to "metrosAcentimetros",
        "Metros a Kilómetros" to "metrosAkilometros"
    )

    private val opsMasa = listOf(
        "Tonelada a Libra" to "toneladasAlibras",
        "Kilogramo a Libra" to "kilogramosAlibras",
        "Gramo a Kilogramo" to "gramosAkilogramos"
    )

    private val opsTemp = listOf(
        "Celsius a Fahrenheit" to "celciusAfahrenheit",
        "Fahrenheit a Celsius" to "fahrenheitAcelsius",
        "Celsius a Kelvin" to "celsiusAkelvin"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)

        session = SessionManager(this)
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
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
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
            showMsg("El valor debe ser un número positivo", false)
            return
        }

        val ops = when (currentTab) {
            Tab.LONGITUD -> opsLongitud
            Tab.MASA -> opsMasa
            Tab.TEMPERATURA -> opsTemp
        }
        val idx = operationSpinner.selectedItemPosition
        if (idx !in ops.indices) {
            showMsg("Seleccione una operación válida", false)
            return
        }

        val tipo = ops[idx].second
        convertButton.isEnabled = false
        convertButton.text = "Procesando…"

        lifecycleScope.launch {
            restClient.callWithFailover(Entrada(value, tipo), object : ec.edu.monster.climov_java_restfull.rest.RestCallback {
                override fun onSuccess(result: ec.edu.monster.climov_java_restfull.models.Resultado) {
                    convertButton.isEnabled = true
                    convertButton.text = "Convertir"
                    if (result.resultado == -1.0) {
                        showMsg(result.mensaje, false)
                    } else {
                        showMsg("Resultado: ${"%.2f".format(result.resultado)}", true)
                    }
                }
                override fun onError(error: String) {
                    convertButton.isEnabled = true
                    convertButton.text = "Convertir"
                    showMsg(error, false)
                }
            })
        }
    }

    // ✅ Función global (no local) — esta es la correcta
    private fun showMsg(msg: String, success: Boolean) {
        resultText.text = msg
        resultText.visibility = View.VISIBLE
        val bg = if (success) R.color.success_bg else R.color.error_bg
        val fg = if (success) R.color.success_text else R.color.error_text
        resultText.setBackgroundColor(ContextCompat.getColor(this, bg))
        resultText.setTextColor(ContextCompat.getColor(this, fg))
    }
}