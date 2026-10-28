
package ca.gbc.comp3074.ali_mukhtar.dinetrack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class AddRestaurantActivity : AppCompatActivity() {


    private var selectedPhotoUri: Uri? = null

    private val photoPickerLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                selectedPhotoUri = uri

                findViewById<ImageView>(R.id.imgRestaurant)
                    .setImageURI(uri)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_restaurant)


        val btnSelectPhoto =
            findViewById<Button>(R.id.btnSelectPhoto)

        btnSelectPhoto.setOnClickListener {
            photoPickerLauncher.launch("image/*")
        }


        val etName = findViewById<EditText>(R.id.etRestaurantName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etCuisine = findViewById<EditText>(R.id.etCuisine)
        val etTags = findViewById<EditText>(R.id.etTags)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val ratingBar = findViewById<RatingBar>(R.id.ratingRestaurant)
        val btnSave = findViewById<Button>(R.id.btnSaveRestaurant)


        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }


        btnSave.setOnClickListener {

            val name = etName.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val cuisine = etCuisine.text.toString().trim()
            val tags = etTags.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val rating = ratingBar.rating

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter a restaurant name and address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("restaurant_name", name)
                putExtra("restaurant_address", address)
                putExtra("restaurant_phone", phone)
                putExtra("restaurant_cuisine", cuisine)
                putExtra("restaurant_tags", tags)
                putExtra("restaurant_description", description)
                putExtra("restaurant_rating", rating)
            }

            setResult(Activity.RESULT_OK, resultIntent)

            Toast.makeText(
                this,
                "Restaurant saved successfully!",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}

