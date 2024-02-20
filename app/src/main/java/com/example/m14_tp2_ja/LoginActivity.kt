package com.example.m14_tp2_ja

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.example.m14_tp2_ja.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    // CRÉATION DE L'ACTIVITÉ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()

        // ÉCOUTEUR DE CLIC POUR LE BOUTON "CONNEXION"
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (checkUserCredentials(username, password)) {
                sharedPreferences.edit().putString("loggedInUsername", username).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showSnackbar()
            }
        }

        // ÉCOUTEUR DE CLIC POUR LE BOUTON "INSCRIPTION"
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    // INITIALISATION DES SHARED PREFERENCES
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // VÉRIFICATION DES IDENTIFIANTS DE L'UTILISATEUR
    private fun checkUserCredentials(username: String, password: String): Boolean {
        val storedPassword = sharedPreferences.getString("${username}_password", null)
        return storedPassword != null && storedPassword == password
    }

    // AFFICHAGE D'UN SNACKBAR
    private fun showSnackbar() {
        Snackbar.make(binding.root, "Nom d'utilisateur ou mot de passe incorrect", Snackbar.LENGTH_SHORT).show()
    }
}