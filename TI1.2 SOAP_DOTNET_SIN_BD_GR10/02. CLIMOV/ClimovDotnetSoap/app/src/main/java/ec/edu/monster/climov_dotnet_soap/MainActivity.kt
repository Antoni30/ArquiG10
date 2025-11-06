package ec.edu.monster.climov_dotnet_soap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirigir inmediatamente al login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}