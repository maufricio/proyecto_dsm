package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MenuSuperiorFragment : Fragment(R.layout.fragment_menu_superior) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cerrarSesion = view.findViewById<MaterialButton>(R.id.btnCerrarSesion)

        cerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut().also {
                Toast.makeText(requireContext(),
                    "Sesión cerrada",
                    Toast.LENGTH_SHORT)
                    .show()



                val intent = Intent(requireContext(), PantallaInicioActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}