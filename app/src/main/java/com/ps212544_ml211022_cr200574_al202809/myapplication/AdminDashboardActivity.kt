package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var imageSaludoDia : ImageView
    private lateinit var spinnerDias : Spinner
    private lateinit var spinnerTiempoComida : Spinner
    private lateinit var btnAgregarComida : com.google.android.material.button.MaterialButton
    private lateinit var noComidasContainer : LinearLayout
    private lateinit var database : DatabaseReference
    private lateinit var linearLayoutRegistros : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_admin)

        //imageView = findViewById(R.id.logo)
       // imageView.setImageResource(R.drawable.logo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageSaludoDia = findViewById(R.id.saludo_tiempoDia)
        imageSaludoDia.setImageResource(R.drawable.image_evening_wbg)
        btnAgregarComida = findViewById(R.id.buttonAgregarComida)
        noComidasContainer = findViewById(R.id.noComidasContainer)
        database = FirebaseDatabase.getInstance().getReference("comidas")
        linearLayoutRegistros = findViewById(R.id.listaRegistroComidas)

        val itemsDias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
        val itemsTiempoComida = listOf("Desayuno", "Almuerzo", "Cena")

        spinnerDias = findViewById(R.id.spinnerDias)
        spinnerDias.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsDias)

        spinnerTiempoComida = findViewById(R.id.spinnerTiemposComida)
        spinnerTiempoComida.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsTiempoComida)


        btnAgregarComida.setOnClickListener {
            // Redirigir a screen de agregar comida
            val intent = Intent(this, AgregarComidasActivity::class.java)
            startActivity(intent)
        }

        /* TODO:
            1.Hacer la búsqueda de items en base a cuando el spinner de día y tiempo de comida cambien
            esto hará que podamos filtrar en base a esos parámetros
            2. Calcular el tiempo actual, y en base a eso hacer el saludo al usuario de: "Buenos días", "Buenas tardes" o "Buenas noches"
            3. Hacer la búsqueda del conteo de cuántos ítems se tienen almacenados en la base de datos para el tiempo de comida.
         */
        this.obtenerRegistros("Miercoles")


    }


    private fun obtenerRegistros(diaComida : String) {
        database.orderByChild("dia").equalTo(diaComida).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                linearLayoutRegistros.removeAllViews() // Esto es para limpiar el LinearLayout
                if (snapshot.exists()) {
                    noComidasContainer.visibility = View.GONE
                    for (registroSnapshot in snapshot.children) {
                        val registro = registroSnapshot.getValue(Registros_Comidas::class.java)
                        registro?.let {
                            it.id = registroSnapshot.key ?: "" // Asignar el ID del registro
                            mostrarRegistro(it)
                        }
                    }
                } else {
                    Toast.makeText(this@AdminDashboardActivity, "No hay  registrados", Toast.LENGTH_SHORT).show()
                    noComidasContainer.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboardActivity, "Error al leer los datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun mostrarRegistro(registro: Registros_Comidas) {
        val view = LayoutInflater.from(this).inflate(
            R.layout.registrocomidaitem,
            linearLayoutRegistros,
            false
        )
        // Declarar e inicializar los campos del view
        val nombreComida = view.findViewById<TextView>(R.id.comidaNombre)
        val descripcionComida = view.findViewById<TextView>(R.id.comidaDescripcion)
        val precioComida = view.findViewById<TextView>(R.id.comidaPrecio)
        val diaComida = view.findViewById<TextView>(R.id.comidaDia)
        val layoutAddStudentItem = view.findViewById<LinearLayout>(R.id.layoutAddStudentItem)


        // Mostrar los datos del registros
        nombreComida.text = "${registro.nombre}"
        descripcionComida.text = "${registro.descripcion}"
        precioComida.text = "$ ${registro.precio}"
        diaComida.text = "${registro.dia} , ${registro.tiempoDia}"

        layoutAddStudentItem.visibility = View.VISIBLE


        // **➡ Agregar evento click al botón de la flecha**
        layoutAddStudentItem.setOnClickListener {
            val intent = Intent(this, DetallesComida_AdministradorActivity::class.java).apply {
                putExtra("nombreComida", registro.nombre)
                putExtra("descripcionComida", registro.descripcion)
                putExtra("precioComida", registro.precio)
                putExtra("diaComida", registro.dia)
                putExtra("tiempoComida", registro.tiempoDia)
            }
            startActivity(intent)
            finish() // Eliminando esta actividad del historial de la RAM
        }


        linearLayoutRegistros.addView(view)

    }
}