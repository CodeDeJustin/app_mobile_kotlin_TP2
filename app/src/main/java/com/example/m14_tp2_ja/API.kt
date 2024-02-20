package com.example.m14_tp2_ja

// IMPORTATION DES BIBLIOTHÈQUES
import android.content.Intent
import retrofit2.Call
import retrofit2.http.GET

// DÉCLARATION DE LA CLASSE DE DONNÉES "FRUIT"
data class Fruit(
    val name: String,
    val id: Int,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutrition
)

// DÉCLARATION DE LA CLASSE DE DONNÉES "NUTRITION"
data class Nutrition(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)

// DÉCLARATION DE L'INTERFACE "FRUITAPI"
interface FruitAPI {
    @GET("fruit/all")
    fun getFruits(): Call<List<Fruit>>
}



