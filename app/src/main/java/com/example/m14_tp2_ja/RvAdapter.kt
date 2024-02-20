package com.example.m14_tp2_ja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ADAPTATEUR POUR LA LISTE DE FRUITS
class FruitAdapter(var fruitList: List<Fruit>) : RecyclerView.Adapter<FruitAdapter.FruitViewHolder>() {


    class FruitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvFamily: TextView = itemView.findViewById(R.id.tvFamily)
        val tvOrder: TextView = itemView.findViewById(R.id.tvOrder)
        val tvGenus: TextView = itemView.findViewById(R.id.tvGenus)
        val tvNutrition: TextView = itemView.findViewById(R.id.tvNutrition)
    }

    // CREATION DU VIEWHOLDER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_api, parent, false)
        return FruitViewHolder(itemView)
    }

    // LIAISON DU VIEWHOLDER AVEC LES DONNEES
    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        val currentItem = fruitList[position]
        holder.tvName.text = holder.itemView.context.getString(R.string.fruit_name, currentItem.name)
        holder.tvFamily.text = holder.itemView.context.getString(R.string.fruit_family, currentItem.family)
        holder.tvOrder.text = holder.itemView.context.getString(R.string.fruit_order, currentItem.order)
        holder.tvGenus.text = holder.itemView.context.getString(R.string.fruit_genus, currentItem.genus)

        val nutritionString = "Calories: ${currentItem.nutritions.calories}, Fat: ${currentItem.nutritions.fat}, Sugar: ${currentItem.nutritions.sugar}, Carbs: ${currentItem.nutritions.carbohydrates}, Protein: ${currentItem.nutritions.protein}"
        holder.tvNutrition.text = holder.itemView.context.getString(R.string.fruit_nutrition, nutritionString)
    }

    // NOMBRE D'ELEMENTS DANS LA LISTE
    override fun getItemCount() = fruitList.size
}