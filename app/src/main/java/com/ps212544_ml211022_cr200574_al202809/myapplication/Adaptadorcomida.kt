package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.Registros_Comidas

class ComidasAdapter(
    private val listaComidas: List<Registros_Comidas>
) : RecyclerView.Adapter<ComidasAdapter.ComidasViewHolder>() {

    class ComidasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewComida)
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombreComida)
        val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecioComida)
        //val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionComida)

        fun bind(comida: Registros_Comidas) {
            textViewNombre.text = comida.nombre
            textViewPrecio.text = "$${comida.precio}"
            //textViewDescripcion.text = comida.descripcion



            Picasso.get()
                .load(comida.url_foto)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error_imagen)
                .into(imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidasViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comida_item, parent, false)
        return ComidasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComidasViewHolder, position: Int) {
        val comida = listaComidas[position]
        holder.bind(comida)
    }

    override fun getItemCount() = listaComidas.size
}