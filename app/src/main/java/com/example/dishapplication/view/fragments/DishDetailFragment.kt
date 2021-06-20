package com.example.dishapplication.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dishapplication.R
import com.example.dishapplication.databinding.FragmentAllDishesBinding
import com.example.dishapplication.databinding.FragmentDishDetailBinding
import java.io.IOException
import java.util.*

class DishDetailFragment : Fragment() {

    private var _binding: FragmentDishDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDishDetailBinding.inflate(inflater,container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .into(binding.imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.tvTitle.text =
                it.dishDetails.type.capitalize(Locale.ROOT) // used to make first letter capital
            binding.tvCategory.text = it.dishDetails.category
            binding.tvType.text = it.dishDetails.type
            binding.tvIngredients.text = it.dishDetails.ingredients
            binding.tvDirection.text = it.dishDetails.directionToCook
            binding.tvCookingTime.text = "Cooking time: ${it.dishDetails.cookingTime}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}