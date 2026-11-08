package ca.gbc.comp3074.ali_mukhtar.sensorlab

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null

    private var temperature by mutableStateOf<Float?>(null)
    private var gyroscope by mutableStateOf<GyroscopeReading?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        setContent {
            MaterialTheme {
                SensorScreen(
                    temperatureAvailable = temperatureSensor != null,
                    temperature = temperature,
                    gyroscopeAvailable = gyroscopeSensor != null,
                    gyroscope = gyroscope
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        temperatureSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        gyroscopeSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> temperature = event.values[0]

            Sensor.TYPE_GYROSCOPE -> {
                gyroscope = GyroscopeReading(
                    x = event.values[0],
                    y = event.values[1],
                    z = event.values[2]
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

private data class GyroscopeReading(
    val x: Float,
    val y: Float,
    val z: Float
)

@Composable
private fun SensorScreen(
    temperatureAvailable: Boolean,
    temperature: Float?,
    gyroscopeAvailable: Boolean,
    gyroscope: GyroscopeReading?
) {
    val temperatureText = when {
        !temperatureAvailable -> "Ambient temperature sensor unavailable"
        temperature == null -> "Waiting for a reading…"
        else -> "%.1f °C".format(temperature)
    }

    val gyroscopeText = when {
        !gyroscopeAvailable -> "Gyroscope unavailable"
        gyroscope == null -> "Waiting for a reading…"
        else -> "X: %.3f rad/s\nY: %.3f rad/s\nZ: %.3f rad/s"
            .format(gyroscope.x, gyroscope.y, gyroscope.z)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SensorLab",
                style = MaterialTheme.typography.headlineMedium
            )

            SensorValueCard(
                title = "Ambient temperature",
                reading = temperatureText
            )

            SensorValueCard(
                title = "Gyroscope",
                reading = gyroscopeText
            )
        }
    }
}

@Composable
private fun SensorValueCard(title: String, reading: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = reading, style = MaterialTheme.typography.bodyLarge)
        }
    }
}