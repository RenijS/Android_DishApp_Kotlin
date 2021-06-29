package com.example.dishapplication.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.FragmentRandomDishBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.model.entities.RandomDish
import com.example.dishapplication.utils.Constants
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory
import com.example.dishapplication.viewmodel.NotificationsViewModel
import com.example.dishapplication.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private lateinit var mRandomDishViewModel: RandomDishViewModel

    private var _binding: FragmentRandomDishBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver(){
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner
            , {randomDishResponse -> randomDishResponse?.let {
                Log.i("Random Dish Response", "$randomDishResponse.recipes[0]")
                setRandomDishResponseInUI(randomDishResponse.recipes[0])
            }}
        )
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner
            , {dataError ->
                dataError?.let {
                    Log.e("Random Dish API error", "$dataError")
                }
            }
        )

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner
            , { loadRandomDish ->
                loadRandomDish?.let {
                    Log.i("Random Dish Loading", "$loadRandomDish")
                }
            })
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe){
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(binding.imageView)

        binding.tvTitle.text = recipe.title
        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            binding.tvType.text = dishType

            binding.tvCategory.text = "Other"
            var ingredients = ""
            for (value in recipe.extendedIngredients){
                if (ingredients.isEmpty()){
                    ingredients = value.original
                } else{
                    ingredients = ingredients + ", \n" + value.original
                }
            }

            binding.tvIngredients.text = ingredients

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                binding.tvDirection.text = Html.fromHtml(
                    recipe.instructions,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else{
                binding.tvDirection.text = Html.fromHtml(recipe.instructions)
            }
            binding.tvCookingTime.text = "Cooking time = ${recipe.readyInMinutes.toString()}"

            binding.imageView2.setOnClickListener {
                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )
                val favDishViewModel : FavDishViewModel by viewModels {
                    FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
                }
                favDishViewModel.insert(randomDishDetails)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}