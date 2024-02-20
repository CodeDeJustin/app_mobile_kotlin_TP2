package com.example.m14_tp2_ja

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m14_tp2_ja.databinding.ActivityInscriptionBinding
import com.google.android.material.snackbar.Snackbar

class InscriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInscriptionBinding
    private lateinit var sharedPreferences: SharedPreferences

    // CRÉATION DE L'ACTIVITÉ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()

        binding.btnInscrire.setOnClickListener {
            registerUser()
        }

        binding.btnAnnulerInscription.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    // INITIALISATION DES SHARED PREFERENCES
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // ENREGISTREMENT DE L'UTILISATEUR
    private fun registerUser() {
        val username = binding.etInscrNomUtilisateur.text.toString()
        val firstName = binding.etInscrPrenom.text.toString()
        val lastName = binding.etInscrNom.text.toString()
        val email = binding.etInscrCourriel.text.toString()
        val password = binding.etInscrMDP.text.toString()
        val confirmPassword = binding.etInscrConfirmMDP.text.toString()

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showSnackbar("Veuillez remplir tous les champs.")
            return
        }

        if (password != confirmPassword) {
            showSnackbar("Les mots de passe ne correspondent pas.")
            return
        }

        if (sharedPreferences.contains("${username}_firstName")) {
            showSnackbar("Le nom d'utilisateur existe déjà.")
            return
        }

        // ENREGISTREMENT DES INFORMATIONS DE L'UTILISATEUR DANS LES SHARED PREFERENCES
        sharedPreferences.edit()
            .putString("${username}_firstName", firstName)
            .putString("${username}_lastName", lastName)
            .putString("${username}_email", email)
            .putString("${username}_password", password)
            .putString("${username}_username", username)
            .apply()

        sharedPreferences.edit().putString("loggedInUsername", username).apply()

        showSnackbar("Inscription réussie!")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // AFFICHAGE D'UN SNACKBAR
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}