package ca.gbc.comp3074.ali_mukhtar.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel

import ca.gbc.comp3074.ali_mukhtar.lab4.data.AppDatabase
import ca.gbc.comp3074.ali_mukhtar.lab4.ui.CustomerViewModel
import ca.gbc.comp3074.ali_mukhtar.lab4.ui.CustomerViewModelFactory
import ca.gbc.comp3074.ali_mukhtar.lab4.ui.CustomerApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the Room database instance
        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            MaterialTheme {

                // Create the ViewModel using the factory
                val viewModel: CustomerViewModel = viewModel(
                    factory = CustomerViewModelFactory(
                        database.customerDao()
                    )
                )

                // Display the customer application
                CustomerApp(viewModel)
            }
        }
    }
}
