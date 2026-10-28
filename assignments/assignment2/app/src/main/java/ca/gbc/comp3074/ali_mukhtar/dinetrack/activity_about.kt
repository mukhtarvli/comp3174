
package ca.gbc.comp3074.ali_mukhtar.dinetrack

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val btnBackHome =
            findViewById<Button>(R.id.btnBackHome)

        btnBackHome.setOnClickListener {
            finish()
        }
    }
}
