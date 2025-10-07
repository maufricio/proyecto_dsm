package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas
import com.squareup.picasso.Picasso

class InformacionDetalladaActivity : AppCompatActivity() {
    private var nombreComida: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_detallada)

        val comidaId = intent.getStringExtra("comida_id")

        if (comidaId != null) {
            obtenerComidaDesdeFirebase(comidaId)
        }

        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, PantallaInicioActivity::class.java)
            startActivity(intent)
            finish()
        }
        val btnPedido = findViewById<Button>(R.id.btnPedido)
        btnPedido.setOnClickListener {
            enviarPedidoPorWhatsApp()
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
                    nombreComida = comida.nombre
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
    private fun enviarPedidoPorWhatsApp() {
        if (nombreComida.isNullOrEmpty()) {
            Toast.makeText(this, "No se ha cargado la información de la comida", Toast.LENGTH_SHORT).show()
            return
        }

        // Número de WhatsApp Business (formato internacional sin +)
        val phoneNumber = "50377284510"

        // Mensaje que aparecerá en WhatsApp
        val message = "¡Hola! 👋 Me gustaría hacer un pedido anticipado de: $nombreComida"

        // Creamos el enlace
        val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

        // Abrimos WhatsApp
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}