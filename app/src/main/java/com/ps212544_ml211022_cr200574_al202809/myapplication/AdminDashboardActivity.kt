package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var imageSaludoDia : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_admin)

        imageView = findViewById(R.id.logo)
        imageView.setImageResource(R.drawable.logo)

        imageSaludoDia = findViewById(R.id.saludo_tiempoDia)
        imageSaludoDia.setImageResource(R.drawable.image_evening_wbg)

    }

}