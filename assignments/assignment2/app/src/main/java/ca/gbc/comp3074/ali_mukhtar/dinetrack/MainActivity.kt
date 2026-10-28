
package ca.gbc.comp3074.ali_mukhtar.dinetrack

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var restaurantList: LinearLayout
    private lateinit var searchRestaurant: EditText

    private data class Restaurant(
        val name: String,
        val address: String,
        val cuisine: String,
        val tags: String,
        val rating: Float
    )

    private val restaurants = mutableListOf<Restaurant>()

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
                val tags =
                    data?.getStringExtra("restaurant_tags") ?: ""
                val rating =
                    data?.getFloatExtra("restaurant_rating", 0f) ?: 0f

                if (name.isNotBlank()) {
                    restaurants.add(
                        Restaurant(
                            name,
                            address,
                            cuisine,
                            tags,
                            rating
                        )
                    )

                    displayRestaurants(
                        searchRestaurant.text.toString()
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        restaurantList = findViewById(R.id.restaurantList)
        searchRestaurant = findViewById(R.id.searchRestaurant)

        val btnViewDetails =
            findViewById<Button>(R.id.btnViewDetails)
        val btnAddRestaurant =
            findViewById<Button>(R.id.btnAddRestaurant)
        val btnAbout =
            findViewById<Button>(R.id.btnAbout)

        btnViewDetails.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RestaurantDetailsActivity::class.java
                )
            )
        }

        btnAddRestaurant.setOnClickListener {
            addRestaurantLauncher.launch(
                Intent(
                    this,
                    AddRestaurantActivity::class.java
                )
            )
        }

        btnAbout.setOnClickListener {
            startActivity(
                Intent(this, AboutActivity::class.java)
            )
        }

        searchRestaurant.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    displayRestaurants(s.toString())
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        displayRestaurants("")
    }

    private fun displayRestaurants(query: String) {

        restaurantList.removeAllViews()

        val filteredRestaurants = restaurants.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.cuisine.contains(query, ignoreCase = true) ||
                    it.tags.contains(query, ignoreCase = true)
        }

        if (filteredRestaurants.isEmpty()) {
            val emptyMessage = TextView(this).apply {
                text = if (restaurants.isEmpty()) {
                    "No saved restaurants yet."
                } else {
                    "No matching restaurants found."
                }
                textSize = 16f
                setPadding(20, 30, 20, 30)
            }

            restaurantList.addView(emptyMessage)
            return
        }

        filteredRestaurants.forEach { restaurant ->

            val restaurantText = TextView(this).apply {
                text = """
                    ${restaurant.name}
                    ${restaurant.address}
                    ${restaurant.cuisine} | ⭐ ${restaurant.rating} / 5
                """.trimIndent()

                textSize = 17f
                setTextColor(
                    android.graphics.Color.parseColor("#222222")
                )
                setPadding(20, 24, 20, 24)
            }

            restaurantList.addView(restaurantText)
        }
    }
}
