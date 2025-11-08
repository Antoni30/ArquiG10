package ec.edu.monster.climov_dotnet_soap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ec.edu.monster.climov_dotnet_soap.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var messageText: TextView
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, ConversionActivity::class.java))
            finish()
            return
        }

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        messageText = findViewById(R.id.messageText)

        loginButton.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        val user = usernameInput.text.toString().trim()
        val pass = passwordInput.text.toString().trim()

        if (user == "MONSTER" && pass == "MONSTER9") {
            session.setLoggedIn(true)
            showMessage("¡Bienvenido!", true)
            messageText.postDelayed({
                startActivity(Intent(this, ConversionActivity::class.java))
                finish()
            }, 600)
        } else {
            showMessage("Credenciales incorrectas. Intente de nuevo.", false)
        }
    }

    private fun showMessage(msg: String, success: Boolean) {
        messageText.text = msg
        messageText.visibility = View.VISIBLE
        val bg = if (success) R.color.success_bg else R.color.error_bg
        val fg = if (success) R.color.success_text else R.color.error_text
        messageText.setBackgroundColor(ContextCompat.getColor(this, bg))
        messageText.setTextColor(ContextCompat.getColor(this, fg))
    }
}