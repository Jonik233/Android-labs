package com.example.myapplication

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

        val editU = findViewById<EditText>(R.id.input_U)
        val editZ = findViewById<EditText>(R.id.input_Z)
        val editR = findViewById<EditText>(R.id.input_R)
        val editX = findViewById<EditText>(R.id.input_X)
        val editT = findViewById<EditText>(R.id.input_t)
        val editKF = findViewById<EditText>(R.id.input_kf)
        val editKThermal = findViewById<EditText>(R.id.input_kThermal)
        val editKDyn = findViewById<EditText>(R.id.input_kDyn)
        val calc_btn = findViewById<Button>(R.id.button_calculate)
        val resultTextView = findViewById<TextView>(R.id.result_text)


        // Розрахунок струму трифазного короткого замикання
        fun calculateThreePhaseCurrent(U: Double, Z: Double): Double {
            // U - фазна напруга, Z - повний опір
            return U / Z
        }

        // Розрахунок струму однофазного короткого замикання
        fun calculateSinglePhaseCurrent(U: Double, R: Double, X: Double): Double {
            // U - фазна напруга, R - активний опір, X - реактивний опір
            val Z = Math.sqrt(R * R + X * X) // Повний опір
            return U / Z
        }

        // Перевірка термічної стійкості
        fun checkThermalStability(I: Double, k: Double, t: Double): Boolean {
            // I - струм короткого замикання, k - коефіцієнт стійкості, t - час
            val I2t = I * I * t
            return I2t <= k
        }

        // Перевірка динамічної стійкості
        fun checkDynamicStability(Ipeak: Double, kdyn: Double): Boolean {
            // Ipeak - піковий струм короткого замикання, kdyn - динамічна стійкість
            return Ipeak <= kdyn
        }

        // Розрахунок пікового струму короткого замикання
        fun calculatePeakCurrent(I: Double, kf: Double): Double {
            // I - струм короткого замикання, kf - коефіцієнт форми
            return I * kf
        }

        fun getValues(): Map<String, Double> {
            val U = editU.text.toString().toDoubleOrNull() ?: 0.0
            val Z = editZ.text.toString().toDoubleOrNull() ?: 0.0
            val R = editR.text.toString().toDoubleOrNull() ?: 0.0
            val X = editX.text.toString().toDoubleOrNull() ?: 0.0
            val T = editT.text.toString().toDoubleOrNull() ?: 0.0
            val KF = editKF.text.toString().toDoubleOrNull() ?: 0.0
            val KThermal = editKThermal.text.toString().toDoubleOrNull() ?: 0.0
            val KDyn = editKDyn.text.toString().toDoubleOrNull() ?: 0.0

            return mapOf(
                "U" to U,
                "Z" to Z,
                "R" to R,
                "X" to X,
                "T" to T,
                "KF" to KF,
                "KThermal" to KThermal,
                "KDyn" to KDyn
            )
        }

        calc_btn.setOnClickListener {
            val values = getValues()
            val U = values["U"] ?: 0.0
            val Z = values["Z"] ?: 0.0
            val R = values["R"] ?: 0.0
            val X = values["X"] ?: 0.0
            val T = values["T"] ?: 0.0
            val KF = values["KF"] ?: 0.0
            val KThermal = values["KThermal"] ?: 0.0
            val KDyn = values["KDyn"] ?: 0.0

            val threePhaseCurrent = calculateThreePhaseCurrent(U, Z)
            val singlePhaseCurrent = calculateSinglePhaseCurrent(U, R, X)
            val isThermalStable = checkThermalStability(threePhaseCurrent, KThermal, T)
            val peakCurrent = calculatePeakCurrent(threePhaseCurrent, KF)
            val isDynamicStable = checkDynamicStability(peakCurrent, KDyn)

            resultTextView.text = """
                                    |threePhaseCurrent: ${"%.1f".format(threePhaseCurrent)}
                                    |singlePhaseCurrent: ${"%.1f".format(singlePhaseCurrent)}
                                    |isThermalStable: $isThermalStable
                                    |isDynamicStable: $isDynamicStable
                                """.trimMargin()
        }
    }

}