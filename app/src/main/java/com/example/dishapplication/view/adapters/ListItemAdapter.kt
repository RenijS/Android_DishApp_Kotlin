package com.example.dishapplication.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dishapplication.databinding.ListRecyclerviewRowBinding
import com.example.dishapplication.view.activities.AddUpdateDishActivity
import com.example.dishapplication.view.fragments.AllDishesFragment

class ListItemAdapter(private val context: Context
, private val fragment: Fragment?
,private val listItems: List<String>
, private val selection: String): RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ListRecyclerviewRowBinding): RecyclerView.ViewHolder(binding.root){
        val tvText = binding.tvText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListRecyclerviewRowBinding = ListRecyclerviewRowBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item

        holder.tvText.setOnClickListener {
            if (context is AddUpdateDishActivity){
                context.selectedListItem(item, selection)
            }
            if (fragment is AllDishesFragment){
                fragment.filterSelection(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}