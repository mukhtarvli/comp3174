package ca.gbc.comp3074.ali_mukhtar.labex5
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GPSLocationScreen()
            }
        }
    }
}

@Composable
fun GPSLocationScreen() {
    val context = LocalContext.current
    val locationManager = remember {
        context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
    }

    var latitude by remember { mutableStateOf("Not available") }
    var longitude by remember { mutableStateOf("Not available") }
    var status by remember { mutableStateOf("Tap to get GPS location") }

    fun getLocation() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            status = "Precise location permission required"
            return
        }

        if (!locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        ) {
            status = "Please enable GPS in Settings"
            return
        }

        status = "Getting GPS location..."

        try {
            locationManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                ContextCompat.getMainExecutor(context)
            ) { location ->
                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    status = "GPS location acquired!"
                } else {
                    status = "Unable to get GPS fix. Try outdoors."
                }
            }
        } catch (e: SecurityException) {
            status = "Location permission denied"
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true
            ) {
                getLocation()
            } else {
                status = "Precise GPS permission is required"
            }
        }

    fun requestLocation() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            getLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GPS Location Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("Latitude")
        Text(
            latitude,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Longitude")
        Text(
            longitude,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { requestLocation() }) {
            Text("Get My Location")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(status)
    }
}
