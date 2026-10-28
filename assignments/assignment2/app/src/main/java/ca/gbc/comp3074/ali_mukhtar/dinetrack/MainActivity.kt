
package ca.gbc.comp3074.ali_mukhtar.dinetrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var restaurantList: LinearLayout

    private val addRestaurantLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val data = result.data

                val name =
                    data?.getStringExtra("restaurant_name") ?: ""

                val address =
                    data?.getStringExtra("restaurant_address") ?: ""

                val cuisine =
                    data?.getStringExtra("restaurant_cuisine") ?: ""

                val rating =
                    data?.getFloatExtra("restaurant_rating", 0f) ?: 0f

                addRestaurantToList(name, address, cuisine, rating)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        restaurantList = findViewById(R.id.restaurantList)

        val btnViewDetails =
            findViewById<Button>(R.id.btnViewDetails)

        val btnAddRestaurant =
            findViewById<Button>(R.id.btnAddRestaurant)

        val btnAbout =
            findViewById<Button>(R.id.btnAbout)

        btnViewDetails.setOnClickListener {
            startActivity(
                Intent(this, RestaurantDetailsActivity::class.java)
            )
        }

        btnAddRestaurant.setOnClickListener {
            val intent = Intent(this, AddRestaurantActivity::class.java)
            addRestaurantLauncher.launch(intent)
        }

        btnAbout.setOnClickListener {
            startActivity(
                Intent(this, AboutActivity::class.java)
            )
        }
    }

    private fun addRestaurantToList(
        name: String,
        address: String,
        cuisine: String,
        rating: Float
    ) {

        if (name.isBlank()) return

        // Remove the initial empty-list message
        if (restaurantList.childCount == 1 &&
            restaurantList.getChildAt(0) is TextView &&
            (restaurantList.getChildAt(0) as TextView).text ==
            "No saved restaurants yet."
        ) {
            restaurantList.removeAllViews()
        }

        val restaurantText = TextView(this).apply {
            text = "$name\n$address\n$cuisine | ⭐ $rating / 5"
            textSize = 17f
            setTextColor(android.graphics.Color.parseColor("#222222"))
            setPadding(20, 24, 20, 24)
        }

        restaurantList.addView(restaurantText)
    }
}

