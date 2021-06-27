package com.example.dishapplication.view.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.FragmentAllDishesBinding
import com.example.dishapplication.databinding.ListDialogBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.utils.Constants
import com.example.dishapplication.view.activities.AddUpdateDishActivity
import com.example.dishapplication.view.activities.MainActivity
import com.example.dishapplication.view.adapters.AllDishesAdapter
import com.example.dishapplication.view.adapters.ListItemAdapter
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory
import com.example.dishapplication.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mAllDishAdapter: AllDishesAdapter

    private lateinit var mCustomListDialog: Dialog

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
        mAllDishAdapter = AllDishesAdapter(this)
        binding.rvAllDishes.adapter = mAllDishAdapter

        mFavDishViewModel.allDishList.observe(viewLifecycleOwner){
            dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        binding.tvNoItem.visibility = View.GONE
                        binding.rvAllDishes.visibility = View.VISIBLE

                        mAllDishAdapter.dishesList(it)
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

    fun dishDetails(favDish : FavDish){
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToDishDetailFragment(favDish))

        if (requireActivity() is MainActivity){
            (activity as MainActivity).hideBottomNavigationView()
        }
    }

    fun deleteDish(dish: FavDish){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Delete Dish")
        builder.setMessage("Are you sure you want to delete?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("YES"){ dialodInterface, _ ->
            mFavDishViewModel.delete(dish)
            dialodInterface.dismiss()
        }
        builder.setNegativeButton("NO"){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog(){
        mCustomListDialog = Dialog(requireActivity())
        val binding: ListDialogBinding = ListDialogBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

        binding.tvListTitle.text= resources.getString(R.string.filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListItemAdapter(requireActivity(), this@AllDishesFragment,dishTypes, Constants.FILTER_SELECTION)

        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity).showBottomNavigationView()
        }
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
            R.id.actionFilterDishes ->{
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun filterSelection(filterItemSelection: String){
        mCustomListDialog.dismiss()

        if (filterItemSelection == Constants.ALL_ITEMS){
            mFavDishViewModel.allDishList.observe(viewLifecycleOwner){
                    dishes ->
                    dishes.let {
                    if (it.isNotEmpty()){
                        binding.tvNoItem.visibility = View.GONE
                        binding.rvAllDishes.visibility = View.VISIBLE

                        mAllDishAdapter.dishesList(it)
                    } else{
                        binding.tvNoItem.visibility = View.VISIBLE
                        binding.rvAllDishes.visibility = View.GONE
                    }
                }
            }
        } else{
            mFavDishViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner){
                dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        binding.rvAllDishes.visibility = View.VISIBLE
                        binding.tvNoItem.visibility = View.GONE

                        mAllDishAdapter.dishesList(it)
                    } else{
                        binding.tvNoItem.visibility = View.VISIBLE
                        binding.rvAllDishes.visibility = View.GONE
                    }
                }

            }
        }
    }
}