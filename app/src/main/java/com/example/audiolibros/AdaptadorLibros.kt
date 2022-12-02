package com.example.audiolibros

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdaptadorLibros(
    val context: Context,
    val libros: List<Libro>
): RecyclerView.Adapter<AdaptadorLibros.LibrosViewHolder>() {

    inner class LibrosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val portada: ImageView = itemView.findViewById(R.id.portadaLibro)
        val titulo: TextView = itemView.findViewById(R.id.tituloLibro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return LibrosViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int) {
        val libro = libros[position]
        holder.titulo.text = libro.titulo
        Glide.with(context)
            .load(libro.imagen)
            .into(holder.portada)

    }

    override fun getItemCount(): Int {
        return libros.size
    }
}