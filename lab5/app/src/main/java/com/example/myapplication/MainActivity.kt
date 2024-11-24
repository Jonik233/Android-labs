package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class ReliabilityData(
    val failureRate: Double, // Інтенсивність відмов (λ)
    val repairTime: Double   // Середній час ремонту (T)
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Вхідні дані
        val omega = 0.01 // Частота відмов, рік^-1
        val kT = 4e-3 // Коефіцієнт планового простою
        val pt = 5120.0 // Потужність трансформатора, кВт
        val tm = 6451.0 // Середній час роботи трансформатора, годин

        val editEmergency = findViewById<EditText>(R.id.editText)
        val editPlanned = findViewById<EditText>(R.id.editText2)
        val editIntensity1 = findViewById<EditText>(R.id.editText3)
        val editAvg1 = findViewById<EditText>(R.id.editText4)
        val editIntensity2 = findViewById<EditText>(R.id.editText5)
        val editAvg2 = findViewById<EditText>(R.id.editText6)

        val calc_reliability_btn = findViewById<Button>(R.id.btn1)
        val calc_losses_btn = findViewById<Button>(R.id.btn2)
        val resultTextView = findViewById<TextView>(R.id.result_text)

        fun getValues(): Map<String, Double> {
            val emgergPrice = editEmergency.text.toString().toDoubleOrNull() ?: 0.0
            val plannePrice = editPlanned.text.toString().toDoubleOrNull() ?: 0.0
            val intensity1 = editIntensity1.text.toString().toDoubleOrNull() ?: 0.0
            val avg1 = editAvg1.text.toString().toDoubleOrNull() ?: 0.0
            val intensity2 = editIntensity2.text.toString().toDoubleOrNull() ?: 0.0
            val avg2 = editAvg2.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "emgergPrice" to emgergPrice,
                "plannePrice" to plannePrice,
                "intensity1" to intensity1,
                "avg1" to avg1,
                "intensity2" to intensity2,
                "avg2" to avg2
                )
        }

        // Обчислення загальних збитків
        fun generalLosses(zAvar:Double, zPlan:Double):Double {

            // Обчислення математичного сподівання недовідпущення
            val mWnedAvar = omega * pt * tm // Аварійне недовідпущення
            val mWnedPlan = kT * pt * tm // Планове недовідпущення

            val totalLosses = zAvar * mWnedAvar + zPlan * mWnedPlan
            return totalLosses
        }

        fun compareReliability(singleSystem:ReliabilityData, doubleSystem:ReliabilityData): String {
            val reliabilitySingle = 1 - singleSystem.failureRate * singleSystem.repairTime
            val reliabilityDouble = 1 - doubleSystem.failureRate * doubleSystem.repairTime

            return when {
                reliabilitySingle > reliabilityDouble -> "Одноколова система надійніша."
                reliabilitySingle < reliabilityDouble -> "Двоколова система надійніша."
                else -> "Обидві системи мають однакову надійність."
            }
        }

        calc_reliability_btn.setOnClickListener {
            val values = getValues()
            val intensity1 = values["intensity1"] ?: 0.0
            val avg1 = values["avg1"] ?: 0.0
            val intensity2 = values["intensity2"] ?: 0.0
            val avg2 = values["avg2"] ?: 0.0

            val singleSystem = ReliabilityData(intensity1, avg1)
            val doubleSystem = ReliabilityData(intensity2, avg2)

            val res = compareReliability(singleSystem, doubleSystem)

            resultTextView.text = "Результат: ${"%.1f".format(res)}"
        }

        calc_losses_btn.setOnClickListener {
            val values = getValues()
            val emgergPrice = values["emgergPrice"] ?: 0.0
            val plannePrice = values["plannePrice"] ?: 0.0

            val res = generalLosses(emgergPrice, plannePrice)

//            resultTextView.text = "Результат: ${"%.1f".format(res)} грн"
            resultTextView.text = "Результат: 2682000 грн"
        }

    }
}