package ec.edu.monster.climov_dotnet_soap.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("CLIMOV_PREFS", Context.MODE_PRIVATE)
    fun setLoggedIn(loggedIn: Boolean) = prefs.edit().putBoolean("isLoggedIn", loggedIn).apply()
    fun isLoggedIn(): Boolean = prefs.getBoolean("isLoggedIn", false)
}