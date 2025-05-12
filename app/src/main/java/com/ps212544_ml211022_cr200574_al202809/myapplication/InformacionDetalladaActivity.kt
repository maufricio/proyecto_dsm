package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas
import com.squareup.picasso.Picasso

class InformacionDetalladaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_detallada)

        val comidaId = intent.getStringExtra("comida_id")

        if (comidaId != null) {
            obtenerComidaDesdeFirebase(comidaId)
        }
    }

    private fun obtenerComidaDesdeFirebase(comidaId: String) {
        val database = FirebaseDatabase.getInstance()
        val comidaRef = database.getReference("comidas").child(comidaId)

        comidaRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val comida = snapshot.getValue(Registros_Comidas::class.java)
                if (comida != null) {
                    val tituloTextView = findViewById<TextView>(R.id.txtTituloDetallado)
                    val descripcionTextView = findViewById<TextView>(R.id.txtInformacionDetallado)
                    val precioTextView = findViewById<TextView>(R.id.txtPrecioDetallado)
                    val imagen = findViewById<ImageView>(R.id.imagenDetallada)

                    tituloTextView.text = comida.nombre
                    descripcionTextView.text = comida.descripcion
                    precioTextView.text = "$${comida.precio}"

                    Picasso.get()
                        .load(comida.url_foto)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error_imagen)
                        .into(imagen)
                }
            } else {
                Toast.makeText(this, "Comida no encontrada", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
        }
    }

}