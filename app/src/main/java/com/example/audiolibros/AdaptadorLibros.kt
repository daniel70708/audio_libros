package com.example.audiolibros

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.audiolibros.databinding.ItemLibroBinding

/**Adaptador que se encagará de llenar nuestro recyclerview de portadas de libros con su
 * respectivo título, recibe el contexto y la lista de libros a mostrar. Recibimos el context ya que
 * lo utilizaremos para que la librería glide pueda colocar la portada el libro*/
class AdaptadorLibros(
    private var libros: List<Libro>,
    private var listener: OnClickListener
): RecyclerView.Adapter<AdaptadorLibros.LibrosViewHolder>() {

    private  lateinit var mContext: Context

    /**Contiene la vistas que queremos modificar (la portada el libro y el título del mismo), estas vistas
     * estan sin personalizar*/
    inner class LibrosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemLibroBinding.bind(itemView)

        fun setListener(position: Int){
            binding.root.setOnClickListener { listener.onItemClick(position) }
        }

    }

    /**Devuelve la vista (sin personalizar) asociada a como se va a ver cada elemento del recyclerview */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_libro, parent, false)
        return LibrosViewHolder(view)
    }

    /**Devuelve el tamaño del conjunto de datos*/
    override fun getItemCount(): Int = libros.size

    /**Personaliza cada elemento con los datos de acuerdo a su posición */
    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int) {

        val libro = libros[position]

        with(holder){
            setListener(position)

            binding.itemNombreLibro.text = libro.Nombre
            Glide.with(mContext)
                .load(libro.Url_imagen)
                .into(binding.itemPortadaLibro)
        }

    }

}