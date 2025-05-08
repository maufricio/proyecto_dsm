package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas

class PantallaInicioActivity : AppCompatActivity() {
    private lateinit var recyclerViewComidas: RecyclerView
    private lateinit var comidasAdapter: ComidasAdapter
    private val listaDeComidas = mutableListOf<Registros_Comidas>()
    private lateinit var database: FirebaseDatabase
    private lateinit var comidasRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantallainicio)
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
        val Titulotiempodecomida = findViewById<TextView>(R.id.titulotiempodecomida)
        val tiempodecomida = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        //Dependiendo la hora del dia se mostrara un titulo diferente
        Titulotiempodecomida.text = when (tiempodecomida) {
            in 0..11 -> "Desayunos > "
            in 12..17 -> "Almuerzos > "
            else -> "Cenas > "
        }

        recyclerViewComidas = findViewById(R.id.recyclerViewComidas)
        recyclerViewComidas.layoutManager = GridLayoutManager(this, 2)

        database = FirebaseDatabase.getInstance()
        comidasRef = database.getReference("comidas")
        comidasAdapter = ComidasAdapter(listaDeComidas)
        recyclerViewComidas.adapter = comidasAdapter
         //obtenemos el tiempo de comida segun el titulo
        val tiempoDeComidaSeleccionado = when (Titulotiempodecomida.text.toString()) {
            "Desayunos > " -> "Desayuno"
            "Almuerzos > " -> "Almuerzo"
            else -> "Cena"
        }
        obtenerComidas(tiempoDeComidaSeleccionado)
    }

    private fun obtenerComidas(tiempoDeComida: String) {
        //arreglo de los dias de la semana
        val diasDeLaSemana = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        // Obtener el día actual
        val diaActual = diasDeLaSemana[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1]

        comidasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaDeComidas.clear()
                for (comidaSnapshot in snapshot.children) {
                    val comida = comidaSnapshot.getValue(Registros_Comidas::class.java)
                    comida?.let {
                        //Mostramos las comidas dependiendo de la hora y dia de la semana
                        if (it.tiempoDia == tiempoDeComida && it.dia == diaActual) {
                        listaDeComidas.add(it)
                        }
                    }
                }
                comidasAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error al obtener datos: ${error.message}")
                Toast.makeText(this@PantallaInicioActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
