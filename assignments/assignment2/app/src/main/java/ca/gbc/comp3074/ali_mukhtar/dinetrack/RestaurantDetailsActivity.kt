
package ca.gbc.comp3074.ali_mukhtar.dinetrack

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class RestaurantDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_restaurant_details)

        val tvRestaurantName =
            findViewById<TextView>(R.id.tvRestaurantName)
        val tvRating =
            findViewById<TextView>(R.id.tvRating)
        val tvCuisine =
            findViewById<TextView>(R.id.tvCuisine)
        val tvAddress =
            findViewById<TextView>(R.id.tvAddress)
        val tvPhone =
            findViewById<TextView>(R.id.tvPhone)
        val tvTags =
            findViewById<TextView>(R.id.tvTags)
        val tvDescription =
            findViewById<TextView>(R.id.tvDescription)

        val imgRestaurant =
            findViewById<ImageView>(R.id.imgRestaurantDetails)

        val btnDirections =
            findViewById<Button>(R.id.btnDirections)
        val btnShare =
            findViewById<Button>(R.id.btnShare)
        val btnBack =
            findViewById<Button>(R.id.btnBack)
        val btnDeleteRestaurant =
            findViewById<Button>(R.id.btnDeleteRestaurant)

        val name =
            intent.getStringExtra("restaurant_name") ?: "Unknown Restaurant"
        val address =
            intent.getStringExtra("restaurant_address") ?: ""
        val phone =
            intent.getStringExtra("restaurant_phone") ?: ""
        val cuisine =
            intent.getStringExtra("restaurant_cuisine") ?: ""
        val tags =
            intent.getStringExtra("restaurant_tags") ?: ""
        val description =
            intent.getStringExtra("restaurant_description") ?: ""
        val rating =
            intent.getFloatExtra("restaurant_rating", 0f)
        val photoUri =
            intent.getStringExtra("restaurant_photo")

        // Display restaurant information
        tvRestaurantName.text = name
        tvRating.text = "⭐ $rating / 5"
        tvCuisine.text = "Cuisine: $cuisine"
        tvAddress.text = "Address: $address"
        tvPhone.text = "Phone: $phone"
        tvTags.text = "Tags: $tags"
        tvDescription.text = description

        // Display the selected restaurant photo
        if (!photoUri.isNullOrBlank()) {
            try {
                imgRestaurant.setImageURI(Uri.parse(photoUri))
                imgRestaurant.visibility = View.VISIBLE
            } catch (e: Exception) {
                imgRestaurant.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Unable to load restaurant photo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            imgRestaurant.visibility = View.GONE
        }

        // Open restaurant address in maps
        btnDirections.setOnClickListener {
            if (address.isBlank()) {
                Toast.makeText(
                    this,
                    "Restaurant address is unavailable",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val uri = Uri.parse(
                    "geo:0,0?q=${Uri.encode(address)}"
                )

                val mapIntent = Intent(Intent.ACTION_VIEW, uri)

                try {
                    startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this,
                        "No maps application found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Share restaurant information
        btnShare.setOnClickListener {
            val shareText = """
                Restaurant: $name
                Cuisine: $cuisine
                Address: $address
                Phone: $phone
                Rating: $rating / 5
                Tags: $tags
                Description: $description
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Restaurant"
                )
            )
        }


        btnDeleteRestaurant.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Restaurant")
                .setMessage("Are you sure you want to delete $name?")
                .setPositiveButton("Delete") { _, _ ->

                    val resultIntent = Intent().apply {
                        putExtra("delete_restaurant", true)
                    }

                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        // Return to Home
        btnBack.setOnClickListener {
            finish()
        }
    }
}
