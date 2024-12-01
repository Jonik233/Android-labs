package com.example.lab6
import kotlin.math.pow
import kotlin.math.sqrt
import android.os.Bundle
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

        val editNaming = findViewById<EditText>(R.id.editText)
        val editCoefN = findViewById<EditText>(R.id.editText2)
        val editCoefP = findViewById<EditText>(R.id.editText3)
        val editStrength = findViewById<EditText>(R.id.editText4)
        val editQuantity = findViewById<EditText>(R.id.editText5)
        val editNomP = findViewById<EditText>(R.id.editText6)
        val editCoefU = findViewById<EditText>(R.id.editText7)
        val editCoefRP = findViewById<EditText>(R.id.editText8)

        val calc_btn = findViewById<Button>(R.id.btn1)
        val resultTextView = findViewById<TextView>(R.id.result_text)

        fun getValues(): Map<String, Double> {
            val naming = editNaming.text.toString().toDoubleOrNull() ?: 0.0
            val CoefN = editCoefN.text.toString().toDoubleOrNull() ?: 0.0
            val CoefP = editCoefP.text.toString().toDoubleOrNull() ?: 0.0
            val Strength = editStrength.text.toString().toDoubleOrNull() ?: 0.0
            val Quantity = editQuantity.text.toString().toDoubleOrNull() ?: 0.0
            val NomP = editNomP.text.toString().toDoubleOrNull() ?: 0.0
            val CoefU = editCoefU.text.toString().toDoubleOrNull() ?: 0.0
            val CoefRP = editCoefRP.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "naming" to naming,
                "CoefN" to CoefN,
                "CoefP" to CoefP,
                "Strength" to Strength,
                "Quantity" to Quantity,
                "NomP" to NomP,
                "CoefU" to CoefU,
                "CoefRP" to CoefRP
            )
        }


        // Розрахунок розрахункового струму
        fun calculateCurrent(n: Int, Pn: Double, Uh: Double, cosPhi: Double, etaH: Double): Double {
            return (n * Pn) / (sqrt(3.0) * Uh * cosPhi * etaH)
        }

        // Розрахунок групового коефіцієнта використання
        fun calculateGroupUtilizationCoefficient(sumNPk: Double, sumNP: Double): Double {
            return sumNPk / sumNP
        }

        // Розрахунок ефективної кількості ЕП
        fun calculateEffectiveNumberEP(sumNP: Double, sumNP2: Double): Double {
            return (sumNP.pow(2)) / sumNP2
        }

        // Розрахунок розрахункової активної потужності
        fun calculateActivePower(Kp: Double, Kb: Double, Pn: Double): Double {
            return Kp * Kb * Pn
        }

        // Розрахунок розрахункової реактивної потужності
        fun calculateReactivePower(Kb: Double, Pn: Double, tgPhi: Double): Double {
            return Kb * Pn * tgPhi
        }

        // Розрахунок повної потужності
        fun calculateFullPower(Pp: Double, Qp: Double): Double {
            return sqrt(Pp.pow(2) + Qp.pow(2))
        }

    }
}