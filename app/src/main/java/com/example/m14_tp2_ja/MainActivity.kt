package com.example.m14_tp2_ja

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.result.contract.ActivityResultContracts
import com.example.m14_tp2_ja.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fruitAdapter: FruitAdapter

    // CRÉATION DE L'ACTIVITÉ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()
        afficheMessageAccueil()

        // LANCEMENT DE L'ACTIVITÉ POUR CHANGER LE MOT DE PASSE
        binding.btnChangerMdP.setOnClickListener {
            lanceurChangementMDP.launch(Intent(this, ChangerMDPActivity::class.java))
        }

        // DÉCONNEXION DE L'UTILISATEUR
        binding.btnDeconnecter.setOnClickListener {
            deconnexion()
        }

        initialiseRecyclerView()
        chargeDonneesFruits()
    }

    // INITIALISATION DES SHARED PREFERENCES
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // AFFICHAGE DU MESSAGE D'ACCUEIL
    private fun afficheMessageAccueil() {
        val username = sharedPreferences.getString("loggedInUsername", "")
        if (!username.isNullOrEmpty()) {
            val firstName = sharedPreferences.getString("${username}_firstName", "")
            val lastName = sharedPreferences.getString("${username}_lastName", "")
            binding.tvBonjourUtilisateur.text = getString(R.string.bonjour_utilisateur, "$firstName $lastName!")
        }
    }

    // DÉCONNEXION DE L'UTILISATEUR
    private fun deconnexion() {
        with(sharedPreferences.edit()) {
            remove("loggedInUsername")
            apply()
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // ENREGISTREMENT DU RÉSULTAT DE L'ACTIVITÉ DE CHANGEMENT DE MOT DE PASSE
    private val lanceurChangementMDP = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            afficheMessageAccueil()
        }
    }

    // INITIALISATION DU RECYCLER VIEW
    private fun initialiseRecyclerView() {
        fruitAdapter = FruitAdapter(listOf())
        binding.rvFruits.layoutManager = LinearLayoutManager(this)
        binding.rvFruits.adapter = fruitAdapter
    }

    // CHARGEMENT DES DONNÉES DE FRUITS
    private fun chargeDonneesFruits() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val fruitApi = retrofit.create(FruitAPI::class.java)

        fruitApi.getFruits().enqueue(object : Callback<List<Fruit>> {

            // TRAITEMENT DE LA RÉPONSE DE L'API
            override fun onResponse(call: Call<List<Fruit>>, response: Response<List<Fruit>>) {
                if (response.isSuccessful) {
                    val fruitData = response.body()
                    if (fruitData != null) {
                        fruitAdapter.fruitList = fruitData
                        fruitAdapter.notifyDataSetChanged()
                    }
                } else {
                    // GESTION DE L'ERREUR DE CHARGEMENT DES DONNÉES
                    if (!isFinishing && !isDestroyed) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Erreur")
                            .setMessage("Erreur lors du chargement des données")
                            .setPositiveButton("Réessayer") { _, _ ->
                                chargeDonneesFruits()
                            }
                            .setNegativeButton("Annuler") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }

            // GESTION DES ERREURS DE L'APPEL API
            override fun onFailure(call: Call<List<Fruit>>,t: Throwable) {
                if (!isFinishing && !isDestroyed) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Erreur")
                        .setMessage("Erreur lors du chargement des données: ${t.localizedMessage}")
                        .setPositiveButton("Réessayer") { _, _ ->
                            chargeDonneesFruits()
                        }
                        .setNegativeButton("Annuler") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        })
    }
}