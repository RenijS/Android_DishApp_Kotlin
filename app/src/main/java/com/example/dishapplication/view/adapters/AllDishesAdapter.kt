package com.example.dishapplication.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapplication.R
import com.example.dishapplication.databinding.RecyclerviewDishLayoutBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.utils.Constants
import com.example.dishapplication.view.activities.AddUpdateDishActivity
import com.example.dishapplication.view.fragments.AllDishesFragment
import com.example.dishapplication.view.fragments.FavouriteDishesFragment

class AllDishesAdapter(private val fragment: Fragment): RecyclerView.Adapter<AllDishesAdapter.ViewHolder>() {

    private var dishList: List<FavDish> = listOf()

    class ViewHolder(binding: RecyclerviewDishLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val tvTitle = binding.tvTitle
        val ivDish = binding.ivDish
        val ibMore = binding.ibMore
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

        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
            else if (fragment is FavouriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }
        //Pop Up Menu
        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.actionEditDish){
                    val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)
                }
                else if (it.itemId == R.id.actionDeleteDish){
                    if (fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                }
                true
            }
            popup.show()
        }

        if (fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        } else if (fragment is FavouriteDishesFragment){
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    fun dishesList(list : List<FavDish>){
        dishList = list
        notifyDataSetChanged()
    }
}