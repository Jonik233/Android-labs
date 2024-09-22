package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference all the EditTexts
        val editTextH = findViewById<EditText>(R.id.editTextH)
        val editTextC = findViewById<EditText>(R.id.editTextC)
        val editTextS = findViewById<EditText>(R.id.editTextS)
        val editTextN = findViewById<EditText>(R.id.editTextN)
        val editTextO = findViewById<EditText>(R.id.editTextO)
        val editTextW = findViewById<EditText>(R.id.editTextW)
        val editTextA = findViewById<EditText>(R.id.editTextA)

        // Reference the buttons and the result TextView
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val resultTextView = findViewById<TextView>(R.id.result_text)

        fun getValues(): Map<String, Double> {

            val H = editTextH.text.toString().toDoubleOrNull() ?: 0.0
            val C = editTextC.text.toString().toDoubleOrNull() ?: 0.0
            val S = editTextS.text.toString().toDoubleOrNull() ?: 0.0
            val N = editTextN.text.toString().toDoubleOrNull() ?: 0.0
            val O = editTextO.text.toString().toDoubleOrNull() ?: 0.0
            val W = editTextW.text.toString().toDoubleOrNull() ?: 0.0
            val A = editTextA.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "H" to H,
                "C" to C,
                "S" to S,
                "N" to N,
                "O" to O,
                "W" to W,
                "A" to A
            )
        }

        // Button listener for calculation of dry mass
        button1.setOnClickListener {

            val values = getValues()

            // Perform the calculation
            val K_dry = 100 / (100 - values["W"]!!)
            val H_dry = values["H"]!! * K_dry
            val C_dry = values["C"]!! * K_dry
            val S_dry = values["S"]!! * K_dry
            val N_dry = values["N"]!! * K_dry
            val O_dry = values["O"]!! * K_dry
            val A_dry = values["A"]!! * K_dry

            // Display the result in the TextView
            resultTextView.text = String.format(
                "Результат розрахунків: \nH: %.2f \nC: %.2f \nS: %.2f \nN: %.2f \nO: %.2f \nA: %.2f",
                H_dry, C_dry, S_dry, N_dry, O_dry, A_dry
            )
        }

        // Button listener for calculation of flammable mass
        button2.setOnClickListener {

            val values = getValues()

            // Perform the calculation
            val K_fl = 100 / (100 - values["W"]!! - values["A"]!!)
            val H_fl = values["H"]!! * K_fl
            val C_fl = values["C"]!! * K_fl
            val S_fl = values["S"]!! * K_fl
            val N_fl = values["N"]!! * K_fl
            val O_fl = values["O"]!! * K_fl

            // Display the result in the TextView
            resultTextView.text = String.format(
                "Результат розрахунків: \nH: %.2f \nC: %.2f \nS: %.2f \nN: %.2f \nO: %.2f",
                H_fl, C_fl, S_fl, N_fl, O_fl
            )
        }

        // Button listener for calculation of lower heat of combustion for flammable mass
        button3.setOnClickListener {

            val values = getValues()
            val C = values["C"]!!
            val H = values["H"]!!
            val O = values["O"]!!
            val S = values["S"]!!
            val W = values["W"]!!
            val A = values["A"]!!

            // Perform the calculation
            val Q_work = 339 * C + 1030 * H - 108.8 * (O - S) - 25 * W
            val Q_fl = (Q_work + 0.025*W) * (100 / (100-W-A))

            // Display the result in the TextView
            resultTextView.text = String.format("Результат розрахунків: \nQ_fl: %.2f", Q_fl)
        }

        // Button listener for calculation of lower heat of combustion for dry mass
        button4.setOnClickListener {

            val values = getValues()
            val C = values["C"]!!
            val H = values["H"]!!
            val O = values["O"]!!
            val S = values["S"]!!
            val W = values["W"]!!

            // Perform the calculation
            val Q_work = 339*C + 1030*H - 108.8*(O-S) - 25*W
            val Q_dry = (Q_work + 0.025 * W) * (100 / (100 - W))

            // Display the result in the TextView
            resultTextView.text = String.format("Результат розрахунків: \nQ_dry: %.2f", Q_dry)
        }

        // Button listener for calculation of lower heat of combustion for working mass
        button5.setOnClickListener {

            val values = getValues()
            val C = values["C"]!!
            val H = values["H"]!!
            val O = values["O"]!!
            val S = values["S"]!!
            val W = values["W"]!!

            // Perform the calculation
            val Q_work = 339*C + 1030*H - 108.8*(O-S) - 25*W

            // Display the result in the TextView
            resultTextView.text = String.format("Результат розрахунків: \nQ_work: %.2f", Q_work)
        }

    }
}