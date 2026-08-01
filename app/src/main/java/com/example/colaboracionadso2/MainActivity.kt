package com.example.colaboracionadso2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.colaboracionadso2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ejemplo: botón para iniciar sesión
        binding.btnLogin.setOnClickListener {

            val usuario = binding.etUsuario.text.toString()
            val clave = binding.etPassword.text.toString()

            hacerLogin(usuario, clave)
        }
    }

    // ---------- PASO A: POST de login ----------
    private fun hacerLogin(usuario: String, clave: String) {

        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.api.login(
                    LoginRequest(usuario, clave)
                )

                if (resp.isSuccessful) {

                    token = resp.body()?.accessToken

                    binding.txtResultado.text = "Login correcto"

                    Log.d("API", "Token recibido: $token")

                    obtenerUsuario()

                } else {

                    binding.txtResultado.text = "Error: ${resp.code()}"

                    Log.e("API", "Código: ${resp.code()}")
                    Log.e("API", "Mensaje: ${resp.errorBody()?.string()}")
                }

            } catch (e: Exception) {

                binding.txtResultado.text = "Error de red"

                Log.e("API", "Error de red: ${e.message}")
            }
        }
    }

    private fun obtenerUsuario() {
        val t = token ?: return
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.api.getCurrentUser("Bearer $t")
                if (resp.isSuccessful) {
                    val user = resp.body()
                    binding.txtResultado.text =
                        "Hola ${user?.firstName}\n${user?.email}"
                    Log.d("API", "Hola ${user?.firstName}")

                } else {
                    binding.txtResultado.text = "Error: ${resp.code()}"
                }
            } catch (e: Exception) {
                binding.txtResultado.text = "Error: ${e.message}"
                Log.e("API", e.message.toString())
            }
        }
    }
}