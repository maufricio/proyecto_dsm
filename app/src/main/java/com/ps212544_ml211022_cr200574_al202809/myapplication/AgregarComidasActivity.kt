package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.CloudinaryResponse
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgregarComidasActivity : AppCompatActivity() {

    private lateinit var guardarBtn : MaterialButton
    private lateinit var nombreComidaField : EditText
    private lateinit var descripcionComidaField : EditText
    private lateinit var precioComidaField : EditText
    private lateinit var diaSpinner : Spinner
    private lateinit var tiempoDiaSpinner : Spinner
    private lateinit var previsualizarImagenText : TextView

    // Contenedores para los modos de captura de fotos
    private lateinit var tomarFotoContainer : LinearLayout
    private lateinit var galeriaFotoContainer : LinearLayout
    private lateinit var cloudinaryService : CloudinaryService

    // Propiedad para la base de datos
    private lateinit var database : DatabaseReference

    // Constantes para la subida de las imagenes
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_PERMISSION = 100
    private var imageUri : Uri? = null

    private var imageUrlSubida : String? = "no hay link"

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
        previsualizarImagenText = findViewById(R.id.previsualizarImagenText)

        // Inicialización de los spinners

        val itemsDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val itemsTiempoComida = listOf("Desayuno", "Almuerzo", "Cena")

        val adapterDias = ArrayAdapter(this, R.layout.spinner_item, itemsDias)
        adapterDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        diaSpinner.adapter = adapterDias

        val adapterTiempoComida = ArrayAdapter(this, R.layout.spinner_item, itemsTiempoComida)
        adapterTiempoComida.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        tiempoDiaSpinner.adapter = adapterTiempoComida


        // Inicialización de Retrofit

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.cloudinary.com/v1_1/dakstut84/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.cloudinaryService = retrofit.create(CloudinaryService::class.java)

        // Cuando se pulse el boton guardar, mandar a llamar: guardarComida()
        guardarBtn.setOnClickListener {
            guardarComida()
        }

        // Texto que cuando se clicke, entonces mandar a la pantalla de previsualizar la imagen junto con una propiedad
        previsualizarImagenText.setOnClickListener{
            val intent = Intent(this, PrevisualizarImagen::class.java)
            intent.putExtra("imageUri", imageUri.toString())
            startActivity(intent)
        }

        galeriaFotoContainer.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permission = android.Manifest.permission.READ_MEDIA_IMAGES

                if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Pidiendo permiso READ_MEDIA_IMAGES", Toast.LENGTH_SHORT).show()
                    // Verifica que debería mostrar un diálogo de solicitud de permiso
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Toast.makeText(this, "Necesitamos acceso a tus imágenes para seleccionar desde galería.", Toast.LENGTH_SHORT).show()
                    }

                    ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION)
                } else {
                    Toast.makeText(this, "Permiso ya concedido", Toast.LENGTH_SHORT).show()
                    abrirGaleria()
                }
            } else {
                val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Pidiendo permiso READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show()
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Toast.makeText(this, "Se necesita permiso para acceder a tus fotos.", Toast.LENGTH_LONG).show()
                    }

                    ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION)
                } else {
                    Toast.makeText(this, "Permiso ya concedido", Toast.LENGTH_SHORT).show()
                    abrirGaleria()
                }
            }
        }


    }

    private fun guardarComida() {
        val nombre = nombreComidaField.text.toString()
        val descripcion = descripcionComidaField.text.toString()
        val precio = precioComidaField.text.toString()
        // Recoger los valores de los spinners
        val dia = diaSpinner.selectedItem.toString()
        val tiempoDia = tiempoDiaSpinner.selectedItem.toString()

        if (nombre.isEmpty() || descripcion.isEmpty() || (precio.isEmpty())) {
            return
        }

        subirImagenACloudinary(imageUri!!) { imageUrl ->
            val comidaId = database.push().key!!
            val comida = Registros_Comidas(comidaId, imageUrl, nombre, descripcion, precio, dia, tiempoDia)

            database.child(comidaId).setValue(comida).addOnSuccessListener {
                Toast.makeText(this, "Registro guardado con éxito", Toast.LENGTH_SHORT).show()
                intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun subirImagenACloudinary(imageUri : Uri, onComplete: (String) -> Unit) {
        val stream = contentResolver.openInputStream(imageUri)
        val fileBytes = stream?.readBytes() ?: return

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
        val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)
        val preset = RequestBody.create("text/plain".toMediaTypeOrNull(), "unsigned_android")

        val call = this.cloudinaryService.uploadImage(body, preset)

        call.enqueue(object : Callback<CloudinaryResponse> {
            override fun onResponse(call : Call<CloudinaryResponse>, response: Response<CloudinaryResponse>) {
                if(response.isSuccessful) {
                    val imageUrl = response.body()?.secureUrl
                    Toast.makeText(this@AgregarComidasActivity, "URL: $imageUrl", Toast.LENGTH_SHORT).show()

                    imageUrlSubida = imageUrl.toString()
                    onComplete(imageUrlSubida.toString()) // Espera que termine de completarse este campo, de manera asíncrona
                } else {
                    Toast.makeText(this@AgregarComidasActivity, "Error al subir la imagen. Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CloudinaryResponse>, t: Throwable) {
                Toast.makeText(this@AgregarComidasActivity, "Falló la subida de la imagen: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            this.imageUri = data.data
            Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
            //Habilitar el texto de "Previsualizar la imagen"
            previsualizarImagenText.visibility = TextView.VISIBLE
            //subirImagenACloudinary(imageUri!!) Funcion para subir la imagen a cloudinary
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirGaleria()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

}