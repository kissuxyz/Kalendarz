package com.example.kalendarz

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startPlan)
        val dayCounter = findViewById<TextView>(R.id.dayCounter)
        val fromPlaceholder = findViewById<TextView>(R.id.fromPlaceholder)
        val tillPlaceholder = findViewById<TextView>(R.id.tillPlaceholder)
        val pricePlaceholder = findViewById<TextView>(R.id.pricePlaceholder)


        startButton.setOnClickListener {
            val drp = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    androidx.core.util.Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )

                )
                .build()
            startButton.setOnClickListener {
                drp.show(supportFragmentManager, "DATE_PICKER_TAG")
            }
            drp.addOnPositiveButtonClickListener {
                val startDate = drp.selection?.first
                val endDate = drp.selection?.second
                if (startDate!! < System.currentTimeMillis()) {
                    dayCounter.text = "- dni"
                    fromPlaceholder.text = ""
                    tillPlaceholder.text = ""

                    Snackbar.make(
                        findViewById(R.id.root),
                        "Prosimy zmienić czas",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (startDate > System.currentTimeMillis() + 63113904000 || endDate!! > System.currentTimeMillis() + 63113904000) {
                    dayCounter.text = "- dni"
                    fromPlaceholder.text = ""
                    tillPlaceholder.text = ""
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Nie możesz zaplanować podróży na więcej niż 2 lata w przyszłość",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    val end = Instant.ofEpochMilli(endDate!!).atZone(ZoneId.systemDefault()).toLocalDate()
                    // from miliseconds, count how many days between start and end
                    val days = end.toEpochDay() - start.toEpochDay()
                    dayCounter.text = "${days.toString()} dni"
                    val startLocalDate = Instant.ofEpochMilli(startDate).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    val endLocalDate = Instant.ofEpochMilli(endDate!!).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    fromPlaceholder.text = "Rozpoczęcie podróży w dniu $startLocalDate"
                    tillPlaceholder.text = "Zakończenie podróży w dniu $endLocalDate"
                    pricePlaceholder.text = "Przewidywana Cena: ${days * 529} zł"
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Poczekaj chwile, właśnie obliczamy twoją podróż w terminie od ${startLocalDate} do ${endLocalDate}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}