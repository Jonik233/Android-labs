package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.PI
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editPower = findViewById<EditText>(R.id.editTextH)
        val editCurrentStd = findViewById<EditText>(R.id.editTextC)
        val editTargetStd = findViewById<EditText>(R.id.editTextS)
        val editCost = findViewById<EditText>(R.id.editTextV)

        val calc_button = findViewById<Button>(R.id.calc_button)
        val resultTextView = findViewById<TextView>(R.id.result_text)

        fun getValues(): Map<String, Double> {
            val Pc = editPower.text.toString().toDoubleOrNull() ?: 0.0
            val sigma1 = editCurrentStd.text.toString().toDoubleOrNull() ?: 0.0
            val sigma2 = editTargetStd.text.toString().toDoubleOrNull() ?: 0.0
            val electricity_cost = editCost.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "power" to Pc,
                "current_std" to sigma1,
                "target_std" to sigma2,
                "cost" to electricity_cost
            )
        }

        // Функція для чисельного інтегрування (метод прямокутників)
        fun integrate(a: Double, b: Double, step: Double, f: (Double) -> Double): Double {
            var sum = 0.0
            var x = a
            while (x < b) {
                sum += f(x) * step
                x += step
            }
            return sum
        }

        // Функція для розрахунку ймовірності розподілу похибки
        fun prob_distribution(p: Double, Pc: Double, sigma: Double): Double {
            return (1 / (sigma * sqrt(2 * PI))) * exp(-((p - Pc).pow(2)) / (2 * sigma.pow(2)))
        }

        // Button listener for calculation
        calc_button.setOnClickListener {
            val values = getValues()
            val power = values["power"] ?: 0.0
            val currentStd = values["current_std"] ?: 0.0
            val targetStd = values["target_std"] ?: 0.0
            val cost = values["cost"] ?: 0.0

            // Розрахунок для вдосконаленої системи прогнозу
            val deltaW = integrate(4.75, 5.25, 0.01) { p -> prob_distribution(p, power, targetStd) }
            val W = power * 24 * deltaW
            var P = W * cost

            val W2 = power * 24 * (1 - deltaW)
            val S = W2 * cost

            // Кінцева сума
            P -= S

            resultTextView.text = "Результат розрахунків: ${"%.1f".format(P)}"
        }
    }
}
