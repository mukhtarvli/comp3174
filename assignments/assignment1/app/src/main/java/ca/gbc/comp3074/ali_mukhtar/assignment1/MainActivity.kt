package ca.gbc.comp3074.ali_mukhtar.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etHoursWorked = findViewById<EditText>(R.id.etHoursWorked)
        val etHourlyRate = findViewById<EditText>(R.id.etHourlyRate)
        val etTaxRate = findViewById<EditText>(R.id.etTaxRate)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnAbout = findViewById<Button>(R.id.btnAbout)

        val tvPay = findViewById<TextView>(R.id.tvPay)
        val tvOvertimePay = findViewById<TextView>(R.id.tvOvertimePay)
        val tvTotalPay = findViewById<TextView>(R.id.tvTotalPay)
        val tvTax = findViewById<TextView>(R.id.tvTax)

        btnCalculate.setOnClickListener {

            val hours = etHoursWorked.text.toString().toDoubleOrNull()
            val hourlyRate = etHourlyRate.text.toString().toDoubleOrNull()
            val taxRate = etTaxRate.text.toString().toDoubleOrNull()

            if (hours == null || hourlyRate == null || taxRate == null) {
                Toast.makeText(
                    this,
                    "Please enter valid values in every field",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var pay: Double
            var overtimePay: Double

            if (hours <= 40) {
                pay = hours * hourlyRate
                overtimePay = 0.0
            } else {
                pay = 40 * hourlyRate
                overtimePay = (hours - 40) * hourlyRate * 1.5
            }

            val totalPay = pay + overtimePay
            val tax = pay * taxRate

            tvPay.text = "Pay: $${String.format(Locale.CANADA, "%.2f", pay)}"
            tvOvertimePay.text =
                "Overtime Pay: $${String.format(Locale.CANADA, "%.2f", overtimePay)}"
            tvTotalPay.text =
                "Total Pay: $${String.format(Locale.CANADA, "%.2f", totalPay)}"
            tvTax.text = "Tax: $${String.format(Locale.CANADA, "%.2f", tax)}"
        }

        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}