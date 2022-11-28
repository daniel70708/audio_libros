package com.example.audiolibros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorLibros(
    var libros: List<Libro>
): RecyclerView.Adapter<AdaptadorLibros.LibrosViewHolder>() {

    inner class LibrosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val portada: ImageView = itemView.findViewById(R.id.portadaLibro)
        val titulo: TextView = itemView.findViewById(R.id.tituloLibro)
        val autor: TextView = itemView.findViewById(R.id.nombreAutor)
        val calificacion: RatingBar = itemView.findViewById(R.id.calificacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return LibrosViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int) {
       holder.portada.setImageResource(libros[position].portada)
        holder.titulo.text = libros[position].titulo
        holder.autor.text = libros[position].autor
        holder.calificacion.numStars = libros[position].calificacion
    }

    override fun getItemCount(): Int {
        return libros.size
    }
}