package com.example.dishapplication.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.FragmentAllDishesBinding
import com.example.dishapplication.view.activities.AddUpdateDishActivity
import com.example.dishapplication.view.adapters.AllDishesAdapter
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory
import com.example.dishapplication.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private var _binding: FragmentAllDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllDishes.layoutManager = GridLayoutManager(requireActivity(), 2)
        val allDishAdapter = AllDishesAdapter(this)
        binding.rvAllDishes.adapter = allDishAdapter

        mFavDishViewModel.allDishList.observe(viewLifecycleOwner){
            dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        binding.tvNoItem.visibility = View.GONE
                        binding.rvAllDishes.visibility = View.VISIBLE

                        allDishAdapter.dishesList(it)
                    } else{
                        binding.tvNoItem.visibility = View.VISIBLE
                        binding.rvAllDishes.visibility = View.GONE
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dishDetails(){
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToDishDetailFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_all_dishes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}