package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var imageSaludoDia : ImageView
    private lateinit var spinnerDias : Spinner
    private lateinit var spinnerTiempoComida : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_admin)

        imageView = findViewById(R.id.logo)
        imageView.setImageResource(R.drawable.logo)

        imageSaludoDia = findViewById(R.id.saludo_tiempoDia)
        imageSaludoDia.setImageResource(R.drawable.image_evening_wbg)

        val itemsDias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
        val itemsTiempoComida = listOf("Desayuno", "Almuerzo", "Cena")

        spinnerDias = findViewById(R.id.spinnerDias)
        spinnerDias.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsDias)

        spinnerTiempoComida = findViewById(R.id.spinnerTiemposComida)
        spinnerTiempoComida.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsTiempoComida)


    }

}