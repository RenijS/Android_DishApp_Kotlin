package com.example.dishapplication.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.FragmentAllDishesBinding
import com.example.dishapplication.databinding.FragmentDishDetailBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.utils.Constants
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory
import java.io.IOException
import java.util.*

class DishDetailFragment : Fragment() {

    private var mFavDishDetails: FavDish? = null

    private var _binding: FragmentDishDetailBinding? = null

    private val favDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share_dish -> {
                val type = "text/plain"
                val subject = "Checkout this dish recipe"
                var extraText = ""
                val shareWith = "Share with"

                mFavDishDetails?.let {
                    var image = ""
                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE){
                        image = it.image
                    }

                    var cookingInstructions = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        cookingInstructions = Html.fromHtml(
                            it.directionToCook,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else{
                        binding.tvDirection.text = Html.fromHtml(it.directionToCook)
                    }
                    extraText = "$image \n" +
                            "\n Title: ${it.title} \n\n Category: ${it.category}" +
                            "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                            "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."
                }
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

        mFavDishDetails = args.dishDetails

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
                it.dishDetails.title.capitalize(Locale.ROOT) // used to make first letter capital
            binding.tvCategory.text = it.dishDetails.category
            binding.tvType.text = it.dishDetails.type
            binding.tvIngredients.text = it.dishDetails.ingredients
            binding.tvDirection.text = it.dishDetails.directionToCook
            binding.tvCookingTime.text = "Cooking time: ${it.dishDetails.cookingTime}"
            if (args.dishDetails.favouriteDish){
                binding.imageView2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite_selected))
            } else{
                binding.imageView2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart))
            }
        }

        binding.imageView2.setOnClickListener {
            args.dishDetails.favouriteDish = !args.dishDetails.favouriteDish
            favDishViewModel.update(args.dishDetails)

            if (args.dishDetails.favouriteDish){
                binding.imageView2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite_selected))
            } else{
                binding.imageView2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}