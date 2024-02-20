package com.example.m14_tp2_ja

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m14_tp2_ja.databinding.ActivityChangerMdpBinding
import com.google.android.material.snackbar.Snackbar


class ChangerMDPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangerMdpBinding
    private lateinit var sharedPreferences: SharedPreferences

    // CRÉATION DE L'ACTIVITÉ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangerMdpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()

        binding.btnChangerMDP.setOnClickListener {
            changeMotDePasse()
        }

        binding.btnChangerMDPAnnuler.setOnClickListener {
            finish()
        }
    }

    // INITIALISATION DES SHARED PREFERENCES
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // CHANGER LE MOT DE PASSE
    private fun changeMotDePasse() {
        val oldPassword = binding.etOldPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmNewPassword = binding.etConfirmNewPassword.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showSnackbar("Veuillez remplir tous les champs.")
            return
        }

        val username = sharedPreferences.getString("loggedInUsername", "")
        val passwordKey = "${username}_password"
        val storedPassword = sharedPreferences.getString(passwordKey, "")

        if (oldPassword != storedPassword) {
            showSnackbar("L'ancien mot de passe est incorrect.")
            return
        }

        if (newPassword != confirmNewPassword) {
            showSnackbar("Les nouveaux mots de passe ne correspondent pas.")
            return
        }

        sharedPreferences.edit().putString(passwordKey, newPassword).apply()
        showSnackbar("Le mot de passe a été changé avec succès.")
        setResult(RESULT_OK)
        finish()
    }

    // AFFICHER UN SNACKBAR
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}