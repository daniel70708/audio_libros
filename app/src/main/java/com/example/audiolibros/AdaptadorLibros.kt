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

/**Adaptador que se encagará de llenar nuestro recyclerview de portadas de libros con su
 * respectivo título, recibe el contexto y la lista de libros a mostrar. Recibimos el context ya que
 * lo utilizaremos para que la librería glide pueda colocar la portada el libro*/
class AdaptadorLibros(
    val context: LibroListaFragment,
    val libros: List<Libro>
): RecyclerView.Adapter<AdaptadorLibros.LibrosViewHolder>() {

    /**Contiene la vistas que queremos modificar (la portada el libro y el título del mismo), estas vistas
     * estan sin personalizar*/
    inner class LibrosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val portada: ImageView = itemView.findViewById(R.id.portadaLibro)
        val titulo: TextView = itemView.findViewById(R.id.tituloLibro)
    }
    /**Devuelve la vista (sin personalizar) asociada a como se va a ver cada elemento del recyclerview */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return LibrosViewHolder(view)
    }
    /**Personaliza cada elemento con los datos de acuerdo a su posición */
    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int) {
        val libro = libros[position]
        holder.titulo.text = libro.titulo
        Glide.with(context)
            .load(libro.imagen)
            .into(holder.portada)

    }
    /**Devuelve el tamaño del conjunto de datos*/
    override fun getItemCount(): Int {
        return libros.size
    }
}