package com.example.dishapplication.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapplication.databinding.RecyclerviewDishLayoutBinding
import com.example.dishapplication.model.entities.FavDish

class AllDishesAdapter(private val fragment: Fragment): RecyclerView.Adapter<AllDishesAdapter.ViewHolder>() {

    private var dishList: List<FavDish> = listOf()

    class ViewHolder(binding: RecyclerviewDishLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val tvTitle = binding.tvTitle
        val ivDish = binding.ivDish
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewDishLayoutBinding.inflate(LayoutInflater.from(fragment.requireContext()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishList[position]

        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDish)

        holder.tvTitle.text = dish.title
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    fun dishesList(list : List<FavDish>){
        dishList = list
        notifyDataSetChanged()
    }
}