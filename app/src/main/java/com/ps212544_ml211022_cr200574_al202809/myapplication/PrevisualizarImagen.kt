package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class PrevisualizarImagen : AppCompatActivity() {

    private lateinit var previewImage : ImageView
    private lateinit var imageUri : String
    private lateinit var buttonRegresar : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_previsualizar_imagen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Iniciando las variables
        this.previewImage = findViewById(R.id.previewImage)
        this.imageUri = intent.getStringExtra("imageUri").toString()
        this.buttonRegresar = findViewById(R.id.buttonRegresar)

        previewImage.setImageURI(Uri.parse(imageUri))

        buttonRegresar.setOnClickListener {
            finish()
        }

    }
}