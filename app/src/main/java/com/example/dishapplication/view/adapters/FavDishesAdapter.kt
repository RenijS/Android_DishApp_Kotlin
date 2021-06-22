package com.example.dishapplication.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapplication.databinding.ListRecyclerviewRowBinding
import com.example.dishapplication.databinding.RecyclerviewDishLayoutBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.view.fragments.FavouriteDishesFragment

class FavDishesAdapter(private val fragment: Fragment): RecyclerView.Adapter<FavDishesAdapter.ViewHolder>(){

    private var dishList: List<FavDish> = listOf()

    class ViewHolder(binding: RecyclerviewDishLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val tvTitle = binding.tvTitle
        val ivDish = binding.ivDish
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewDishLayoutBinding.inflate(LayoutInflater.from(fragment.requireContext()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishList[position]

        holder.tvTitle.text = dish.title
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDish)

        holder.itemView.setOnClickListener {
            if (fragment is FavouriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    fun dishesList(list: List<FavDish>){
        dishList = list
        notifyDataSetChanged()
    }
}