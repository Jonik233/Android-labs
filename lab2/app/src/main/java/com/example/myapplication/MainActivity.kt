package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val coalLowerHeatingValue = 20.47
        val coalAshContent = 25.20
        val gCoal = 1.5
        val gMazut = 0.0
        val mazutLowerHeatingValue = 40.40
        val mazutAshContent = 0.15
        val efficiencyDustRemoval = 0.985
        val aVinCoal = 0.80
        val aVinMazut = 1.00

        // Reference all the EditTexts
        val editTextH = findViewById<EditText>(R.id.editTextH)
        val editTextC = findViewById<EditText>(R.id.editTextC)
        val editTextS = findViewById<EditText>(R.id.editTextS)

        // Reference the buttons and the result TextView
        val gas_button = findViewById<Button>(R.id.gas)
        val coal_button = findViewById<Button>(R.id.coal)
        val oil_button = findViewById<Button>(R.id.oil)
        val resultTextView = findViewById<TextView>(R.id.result_text)

        fun getValues(): Map<String, Double> {

            val coal_mass = editTextH.text.toString().toDoubleOrNull() ?: 0.0
            val mazut_mass = editTextC.text.toString().toDoubleOrNull() ?: 0.0
            val gas_mass = editTextS.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "gas" to gas_mass,
                "coal" to coal_mass,
                "mazut" to mazut_mass,
            )
        }

        // Function to calculate the emission factor for solid particles (g/GJ)
        fun calcEmissionFactor(ashContent: Double, aVin: Double, lowerHeatingValue: Double, g:Double): Double {
            return (1e6 * aVin * ashContent * (1-efficiencyDustRemoval)) / (lowerHeatingValue*(100-g))
        }

        // Function to calculate total emissions (tonnes)
        fun calcEmissions(emissionFactor: Double, fuelMass: Double, lowerHeatingValue: Double): Double {
            val emissions = 1e-6*emissionFactor*lowerHeatingValue*fuelMass
            return emissions
        }

        // Button listener for calculation of gas emissions
        gas_button.setOnClickListener {

            // For natural gas, solid particle emissions are considered zero.
            val gasEmissions = 0.0

            resultTextView.text = String.format("Результат розрахунків: %.2f", gasEmissions)
        }

        // Button listener for calculation of coal emissions
        coal_button.setOnClickListener {

            val values = getValues()
            val coal_mass = values["coal"]!!
            val coalEmissionFactor = calcEmissionFactor(coalAshContent, aVinCoal, coalLowerHeatingValue, gCoal)
            val coalEmissions = calcEmissions(coalEmissionFactor, coal_mass, coalLowerHeatingValue)

            resultTextView.text = String.format("Результат розрахунків: %.2f", coalEmissions)
        }

        // Button listener for calculation of mazut emissions
        oil_button.setOnClickListener {

            val values = getValues()
            val mazut_mass = values["mazut"]!!
            val mazutEmissionFactor = calcEmissionFactor(mazutAshContent, aVinMazut, mazutLowerHeatingValue, gMazut)
            val mazutEmissions = calcEmissions(mazutEmissionFactor, mazut_mass, mazutLowerHeatingValue)

            resultTextView.text = String.format("Результат розрахунків: %.2f", mazutEmissions)
        }
    }
}