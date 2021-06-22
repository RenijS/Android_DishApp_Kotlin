package com.example.dishapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.FragmentFavouriteDishesBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.view.activities.MainActivity
import com.example.dishapplication.view.adapters.FavDishesAdapter
import com.example.dishapplication.viewmodel.DashboardViewModel
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory

class FavouriteDishesFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var _binding: FragmentFavouriteDishesBinding? = null

    private val binding get() = _binding!!

    private val favDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteDishesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavDish.layoutManager = GridLayoutManager(requireContext(), 2)
        val favDishAdapter = FavDishesAdapter(this)
        binding.rvFavDish.adapter = favDishAdapter

        favDishViewModel.favouriteDishes.observe(viewLifecycleOwner){
            dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        binding.textNoFavouriteDishes.visibility = View.GONE
                        binding.rvFavDish.visibility = View.VISIBLE

                        favDishAdapter.dishesList(it)
                    } else{
                        binding.textNoFavouriteDishes.visibility = View.VISIBLE
                        binding.rvFavDish.visibility = View.GONE
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity){
            (activity as MainActivity).showBottomNavigationView()
        }
    }

    fun dishDetails(favDish: FavDish){
        findNavController().navigate(FavouriteDishesFragmentDirections.actionNavigationFavouriteDishesToDishDetailFragment(favDish))

        if (requireActivity() is MainActivity){
            (activity as MainActivity).hideBottomNavigationView()
        }
    }
}