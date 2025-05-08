package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas

class AgregarComidasActivity : AppCompatActivity() {

    private lateinit var guardarBtn : MaterialButton
    private lateinit var nombreComidaField : EditText
    private lateinit var descripcionComidaField : EditText
    private lateinit var precioComidaField : EditText
    private lateinit var diaSpinner : Spinner
    private lateinit var tiempoDiaSpinner : Spinner

    // Contenedores para los modos de captura de fotos
    private lateinit var tomarFotoContainer : LinearLayout
    private lateinit var galeriaFotoContainer : LinearLayout

    // Propiedad para la base de datos
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_comidas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Inicializacion de variables
        database = FirebaseDatabase.getInstance().getReference("comidas")
        guardarBtn = findViewById(R.id.guardarComidaButton)
        nombreComidaField = findViewById(R.id.nombreComidaField)
        descripcionComidaField = findViewById(R.id.descripcionComidaField)
        precioComidaField = findViewById(R.id.precioComidaField)
        diaSpinner = findViewById(R.id.diaSpinner)
        tiempoDiaSpinner = findViewById(R.id.tiempoDiaSpinner)
        tomarFotoContainer = findViewById(R.id.tomarFotoContainer)
        galeriaFotoContainer = findViewById(R.id.galeriaFotoContainer)

        val itemsDias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
        val itemsTiempoComida = listOf("Desayuno", "Almuerzo", "Cena")

        diaSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsDias)
        tiempoDiaSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsTiempoComida)

        // Cuando se pulse el boton guardar, mandar a llamar: guardarComida()
        guardarBtn.setOnClickListener {
            guardarComida()
        }

    }

    private fun guardarComida() {
        val nombre = nombreComidaField.text.toString()
        val descripcion = descripcionComidaField.text.toString()
        val precio = precioComidaField.text.toString()
        // Recoger los valores de los spinners
        val dia = diaSpinner.selectedItem.toString()
        val tiempoDia = tiempoDiaSpinner.selectedItem.toString()

        if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            return
        }
        // Al hacer key!! significa que no puede ser nulo
        val comidaId = database.push().key!!
        val comida = Registros_Comidas(comidaId, "", nombre, descripcion, precio, dia, tiempoDia)
        database.child(comidaId).setValue(comida).addOnSuccessListener {
            Toast.makeText(this, "Registro guardado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
        }
    }

}