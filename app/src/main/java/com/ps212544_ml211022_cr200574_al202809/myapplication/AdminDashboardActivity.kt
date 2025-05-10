package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
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
import com.squareup.picasso.Picasso

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var imageSaludoDia : ImageView
    private lateinit var spinnerDias : Spinner
    private lateinit var spinnerTiempoComida : Spinner
    private lateinit var btnAgregarComida : com.google.android.material.button.MaterialButton
    private lateinit var noComidasContainer : LinearLayout
    private lateinit var database : DatabaseReference
    private lateinit var linearLayoutRegistros : LinearLayout
    private lateinit var listaComidas : ArrayList<Registros_Comidas>
    private lateinit var listaComidasFiltradas : ArrayList<Registros_Comidas>
    private lateinit var scrollViewComidas : ScrollView


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
        imageSaludoDia.setImageResource(R.drawable.image_evening_wbg) // Se agrega la imagen del saludo. Posteriormente tiene que ser condicional
        btnAgregarComida = findViewById(R.id.buttonAgregarComida)
        noComidasContainer = findViewById(R.id.noComidasContainer)
        database = FirebaseDatabase.getInstance().getReference("comidas")
        linearLayoutRegistros = findViewById(R.id.listaRegistroComidas)
        scrollViewComidas = findViewById(R.id.scrollViewComidas)

        val itemsDias =
            listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
        val itemsTiempoComida = listOf("Desayuno", "Almuerzo", "Cena")

        spinnerDias = findViewById(R.id.spinnerDias)
        spinnerDias.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsDias)

        spinnerTiempoComida = findViewById(R.id.spinnerTiemposComida)
        spinnerTiempoComida.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsTiempoComida)


        btnAgregarComida.setOnClickListener {
            // Redirigir a screen de agregar comida
            val intent = Intent(this, AgregarComidasActivity::class.java)
            startActivity(intent)
        }

        this.listaComidas = ArrayList()
        this.listaComidasFiltradas = ArrayList()
        obtenerRegistros(spinnerDias.selectedItem.toString(), this.listaComidas, this.listaComidasFiltradas)

        spinnerDias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listaComidas.clear()
                listaComidasFiltradas.clear()
                // Call obtenerRegistros cuando un nuevo item es seleccionado
                obtenerRegistros(
                    spinnerDias.selectedItem.toString(),
                    listaComidas,
                    listaComidasFiltradas
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }

        spinnerTiempoComida.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long) {
                listaComidasFiltradas.clear()
                filtrarComidas(listaComidas, listaComidasFiltradas)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
    }


    private fun obtenerRegistros(diaComida : String,
                                 listaComidas : ArrayList<Registros_Comidas>,
                                 listaComidasFiltradas: ArrayList<Registros_Comidas>
    ) {
        database.orderByChild("dia").equalTo(diaComida).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaComidas.clear()
                linearLayoutRegistros.removeAllViews()
                if (snapshot.exists()) {
                    for (registroSnapshot in snapshot.children) {
                        val registro = registroSnapshot.getValue(Registros_Comidas::class.java)
                        registro?.let {
                            it.id = registroSnapshot.key ?: "" // Asignar el ID del registro
                            //mostrarRegistro(it)
                            listaComidas.add(it)
                        }
                    }
                    Toast.makeText(this@AdminDashboardActivity, "Sí hay comidas para el día: ${diaComida}", Toast.LENGTH_SHORT).show()
                    filtrarComidas(listaComidas, listaComidasFiltradas) // Mandamos a llamar esta función aquí porque es cuando ya todos los datos han sido traídos de la base de datos
                } else {
                    Toast.makeText(this@AdminDashboardActivity, "No hay comidas registradas para el día: ${diaComida}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboardActivity, "Error al leer los datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recorrerComidas(listaComidas : ArrayList<Registros_Comidas>) {
        for (comida in listaComidas) {
            mostrarRegistro(comida)
        }
    }

    private fun filtrarComidas(listaComidas : ArrayList<Registros_Comidas>, listaFiltradaComidas : ArrayList<Registros_Comidas>) {
        for (comida in listaComidas) {
            if (comida.tiempoDia == spinnerTiempoComida.selectedItem.toString()) {
                listaFiltradaComidas.add(comida)
            }
        }

        // lógica de data displaying
        linearLayoutRegistros.removeAllViews() // Esto es para limpiar el LinearLayout
        if (listaFiltradaComidas.isEmpty()) {
            linearLayoutRegistros.visibility = View.GONE
            scrollViewComidas.visibility = View.GONE
            noComidasContainer.visibility = View.VISIBLE
        } else {
            noComidasContainer.visibility = View.GONE
            scrollViewComidas.visibility = View.VISIBLE
            linearLayoutRegistros.visibility = View.VISIBLE
            recorrerComidas(listaFiltradaComidas)
        }
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
        val imagenComida = view.findViewById<ImageView>(R.id.imagenComida)
        val layoutAddStudentItem = view.findViewById<LinearLayout>(R.id.layoutAddStudentItem)


        // Mostrar los datos del registros
        nombreComida.text = "${registro.nombre}"
        descripcionComida.text = "${registro.descripcion}"
        precioComida.text = "$ ${registro.precio}"
        diaComida.text = "${registro.dia} , ${registro.tiempoDia}"

        Toast.makeText(this@AdminDashboardActivity, "Esta es la URL traída: ${registro.url_foto}", Toast.LENGTH_SHORT).show()

        if(registro.url_foto == "no hay link" || registro.url_foto == null || registro.url_foto == "") {
            Toast.makeText(this@AdminDashboardActivity, "Error debido a que no hay imagenes", Toast.LENGTH_SHORT).show()
        } else {
            Picasso.get()
                .load(registro.url_foto) // URL de Cloudinary que viene desde Firebase
                .into(imagenComida)
        }


        layoutAddStudentItem.visibility = View.VISIBLE


        // **➡ Agregar evento click al botón de la flecha**
        layoutAddStudentItem.setOnClickListener {
            val intent = Intent(this, DetallesComida_AdministradorActivity::class.java).apply {
                putExtra("nombreComida", registro.nombre)
                putExtra("descripcionComida", registro.descripcion)
                putExtra("precioComida", registro.precio)
                putExtra("diaComida", registro.dia)
                putExtra("tiempoComida", registro.tiempoDia)
                putExtra("url_foto", registro.url_foto)
            }
            startActivity(intent)
        }


        linearLayoutRegistros.addView(view)
    }
}