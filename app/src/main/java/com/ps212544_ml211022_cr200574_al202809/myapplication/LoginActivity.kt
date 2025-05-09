package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button

    companion object {
        const val LOGIN_SUCCESSFUL_REQUEST_CODE = 1
        const val RESULT_LOGIN_SUCCESSFUL = Activity.RESULT_OK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtPassword).text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext,
                    "Por favor, ingrese su correo electrónico y contraseña",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            this.login(email,password)
        }
    }

    private fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){

                    setResult(RESULT_LOGIN_SUCCESSFUL)

                    Toast.makeText(applicationContext,
                        "Inicio de sesión exitoso",
                        Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(this, AdminDashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener{ exception ->
                Toast.makeText(applicationContext,
                    "Error al iniciar sesión: ${exception.message}",
                    Toast.LENGTH_LONG)
                    .show()
            }
    }
}