package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class MenuSuperiorFragment : Fragment(R.layout.fragment_menu_superior) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cerrarSesion = view.findViewById<MaterialButton>(R.id.btncerrarsesion)

        cerrarSesion.setOnClickListener {
            // Acción de logout, por ejemplo:
            Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show()
            // Podrías hacer finish(), borrar token, etc.
        }
    }
}