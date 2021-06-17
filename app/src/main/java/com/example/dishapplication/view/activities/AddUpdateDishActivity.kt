package com.example.dishapplication.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.audiofx.BassBoost
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dishapplication.R
import com.example.dishapplication.application.FavDishApplication
import com.example.dishapplication.databinding.ActivityAddUpdateDishBinding
import com.example.dishapplication.databinding.AddImageDialogBinding
import com.example.dishapplication.databinding.ListDialogBinding
import com.example.dishapplication.model.entities.FavDish
import com.example.dishapplication.utils.Constants
import com.example.dishapplication.view.adapters.ListItemAdapter
import com.example.dishapplication.viewmodel.FavDishViewModel
import com.example.dishapplication.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import java.util.jar.Manifest

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""

    private lateinit var mCustomListDialog: Dialog

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "FavDishImages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        binding.ivAddImage.setOnClickListener(this)

        binding.etType.setOnClickListener(this)

        binding.etCategory.setOnClickListener(this)

        binding.etCookingTime.setOnClickListener(this)

        binding.buttonAdd.setOnClickListener(this)

    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ivAddImage ->{
                customImageSelectionDialog()
                return
            }

            R.id.etType ->{
                customItemsDialog("SELECT DISH TYPE"
                    ,Constants.dishTypes()
                    ,Constants.DISH_TYPE)
                return
            }

            R.id.etCategory ->{
                customItemsDialog("SELECT CATEGORY TYPE"
                    ,Constants.dishCategory()
                    ,Constants.DISH_CATEGORY)
                return
            }

            R.id.etCookingTime ->{
                customItemsDialog("SELECT COOKING TIME TYPE"
                    ,Constants.dishCookTime()
                    ,Constants.DISH_COOKING_TIME)
                return
            }

            R.id.buttonAdd ->{
                val title = binding.etTitle.toString().trim{it <= ' '}
                val type = binding.etType.toString().trim { it <= ' '}
                val category = binding.etCategory.toString().trim{it <= ' '}
                val ingredients = binding.etIngredients.toString().trim { it <= ' '}
                val cookingTimeInMinutes = binding.etCookingTime.toString().trim{it <= ' '}
                val cookingDirection = binding.etDirections.toString().trim { it <= ' '}

                when{
                    //checking empty or not
                    TextUtils.isEmpty(mImagePath) -> {
                        Toast.makeText(this, "Dish image not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(this, "Dish title not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(type) ->{
                        Toast.makeText(this, "Dish type not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(category) -> {
                        Toast.makeText(this, "Dish category not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(ingredients) -> {
                        Toast.makeText(this, "Dish ingredients not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(cookingTimeInMinutes) ->{
                        Toast.makeText(this, "Dish cooking time not selected", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(cookingDirection) ->{
                        Toast.makeText(this, "Dish cooking direction time not selected", Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        val favDishDetails: FavDish = FavDish(mImagePath,
                            Constants.DISH_IMAGE_SOURCE_LOCAL,
                            title,
                            type,
                            category,
                            ingredients,
                            cookingTimeInMinutes,
                            cookingDirection,
                            false
                        )
                        mFavDishViewModel.insert(favDishDetails)
                        Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                        Log.i("Insertion", "Success $favDishDetails")
                        Log.i("MYTITLE", "${favDishDetails.title}")
                        finish()
                    }

                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding2 = AddImageDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding2.root)

        binding2.ibCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
             //   android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    //alert dialog
                    showRationalDialogForPermission()
                }
            }).onSameThread().check()

            dialog.dismiss()
        }
        binding2.ibGallery.setOnClickListener {
            Dexter.withContext(this).withPermission(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                    startActivityForResult(intent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@AddUpdateDishActivity, "You have denied gallery permission", Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    //binding.ivDishImage.setImageBitmap(thumbnail)

                    //Using Glide(its a open source image loader library) to set image
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(binding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", mImagePath)

                    binding.ivAddImage.setImageResource(R.drawable.ic_edit)
                }
            }
            if (requestCode == GALLERY) {
                data?.let {
                    val selectedPhotoUri = data.data
                    //binding.ivDishImage.setImageURI(selectedPhotoUri)

                    //Using Glide
                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                Log.e("GALLERY", "Error loading")
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                }
                                return false

                            }

                        })
                        .into(binding.ivDishImage)

                    binding.ivAddImage.setImageResource(R.drawable.ic_edit)
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Cancelled", "User cancelled")
        }
    }

    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this).setMessage("It look like you have turned off required permission, you can enable from setting")
            .setPositiveButton("Go To Setting")
            {_,_ ->
                try {
                    //goes to setting
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    //specifies direction to our application
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e : ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("Cancel"){
                dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String{
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun customItemsDialog(title: String, itemsList: List<String>, selection: String){
        mCustomListDialog = Dialog(this)
        val binding: ListDialogBinding = ListDialogBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

        binding.tvListTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = ListItemAdapter(this, itemsList, selection)
        mCustomListDialog.show()
    }

    fun selectedListItem(item: String, selection: String){
        when(selection){
            Constants.DISH_TYPE ->{
                mCustomListDialog.dismiss()
                binding.etType.setText(item)
            }
            Constants.DISH_CATEGORY ->{
                mCustomListDialog.dismiss()
                binding.etCategory.setText(item)
            }
            else ->{
                mCustomListDialog.dismiss()
                binding.etCookingTime.setText(item)
            }
        }
    }
}