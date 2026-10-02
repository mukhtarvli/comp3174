package ca.gbc.comp3074.ali_mukhtar.labex2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var output = 0
    private var stepAmount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val textViewOutput = findViewById<TextView>(R.id.textViewOutput)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonSubtract = findViewById<Button>(R.id.buttonSubtract)
        val buttonReset = findViewById<Button>(R.id.buttonReset)
        val buttonStep = findViewById<Button>(R.id.buttonStep)

        buttonAdd.setOnClickListener {
            output += stepAmount
            textViewOutput.text = output.toString()
        }

        buttonSubtract.setOnClickListener {
            output -= stepAmount
            textViewOutput.text = output.toString()
        }

        buttonReset.setOnClickListener {
            output = 0
            stepAmount = 1
            textViewOutput.text = output.toString()
        }

        buttonStep.setOnClickListener {
            stepAmount = 2
        }
    }
}