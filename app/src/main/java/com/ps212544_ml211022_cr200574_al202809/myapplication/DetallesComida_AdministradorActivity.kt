package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas
import com.squareup.picasso.Picasso

class DetallesComida_AdministradorActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var modificarComidaButton : MaterialButton
    private lateinit var eliminarComidaButton : MaterialButton
    private lateinit var btnRegresarAtras : ImageView

    private lateinit var comidaId : String
    private lateinit var comidaNombre : String
    private lateinit var comidaDescripcion : String
    private lateinit var comidaPrecio : String
    private lateinit var comidaDia : String
    private lateinit var comidaTiempoDia : String
    private lateinit var comidaImagen : String

    //Declarando los controles
    private lateinit var nombreComidaDetalle : EditText
    private lateinit var descripcionComidaDetalle : EditText
    private lateinit var precioComidaDetalle : EditText
    private lateinit var diaComidaDetalle : TextView
    private lateinit var tiempoComidaDetalle : TextView

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_comida_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Inicializar variables
        imageView = findViewById(R.id.imagenDetallesComida)
        modificarComidaButton = findViewById(R.id.modificarComidaButton)
        eliminarComidaButton = findViewById(R.id.eliminarComidaButton)
        nombreComidaDetalle = findViewById(R.id.nombreComidaDetalle)
        descripcionComidaDetalle = findViewById(R.id.descripcionComidaDetalle)
        precioComidaDetalle = findViewById(R.id.precioComidaDetalle)
        diaComidaDetalle = findViewById(R.id.diaComidaDetalle)
        tiempoComidaDetalle = findViewById(R.id.tiempoComidaDetalle)
        btnRegresarAtras = findViewById(R.id.btnRegresarAtras)
        database = FirebaseDatabase.getInstance().getReference("comidas")

        // Capturar los datos del intent

        comidaId = intent.getStringExtra("idComida").toString()
        comidaNombre = intent.getStringExtra("nombreComida").toString()
        comidaDescripcion = intent.getStringExtra("descripcionComida").toString()
        comidaPrecio = intent.getStringExtra("precioComida").toString()
        comidaDia = intent.getStringExtra("diaComida").toString()
        comidaTiempoDia = intent.getStringExtra("tiempoComida").toString()
        comidaImagen = intent.getStringExtra("url_foto").toString()

        // Mostrar los datos en la vista

        Picasso.get().load(comidaImagen).into(imageView)
        nombreComidaDetalle.setText(comidaNombre)
        descripcionComidaDetalle.setText(comidaDescripcion)
        precioComidaDetalle.setText(comidaPrecio)
        diaComidaDetalle.setText(comidaDia)
        tiempoComidaDetalle.setText(comidaTiempoDia)

        btnRegresarAtras.setOnClickListener {
            finish()
        }

        modificarComidaButton.setOnClickListener {
            modificarComida()
        }

        eliminarComidaButton.setOnClickListener {
            eliminarComida()
        }
    }

    private fun modificarComida() {
        val nombre = nombreComidaDetalle.text.toString()
        val descripcion = descripcionComidaDetalle.text.toString()
        val precio = precioComidaDetalle.text.toString()

        if (nombre.isEmpty() || descripcion.isEmpty() || (precio.isEmpty())) {
            return
        }
        val comida = Registros_Comidas(comidaId, comidaImagen, nombre, descripcion, precio, comidaDia, comidaTiempoDia)
        val comidaMap = comida.toMap()
        database.child(comidaId).updateChildren(comidaMap).addOnSuccessListener {
            Toast.makeText(this, "Has actualizado la comida ${nombreComidaDetalle} con éxito", Toast.LENGTH_SHORT).show()
            intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al actualizar la comida", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun eliminarComida() {
        database.child(comidaId).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Has eliminado la comida ${nombreComidaDetalle} con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar la comida", Toast.LENGTH_SHORT).show()
        }
    }


}