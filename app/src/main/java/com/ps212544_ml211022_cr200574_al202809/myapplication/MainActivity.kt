package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // boton para login
        val botonlogin = findViewById<ImageView>(R.id.botonlogin)
        botonlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
        }
        val botontiempodecomida = findViewById<Button>(R.id.botontiempodecomida)
        val tiempodecomida = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        //Dependiendo la hora del dia se mostrara un titulo diferente
        botontiempodecomida.text = when (tiempodecomida) {
            in 0..11 -> "Desayunos > "
            in 12..17 -> "Almuerzos > "
            else -> "Cenas > "
        }


    }
}