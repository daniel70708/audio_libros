package com.example.audiolibros

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.InternalChannelz.instance
import io.grpc.util.TransmitStatusRuntimeExceptionInterceptor.instance


class LibroListaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaLibros: ArrayList<Libro>
    private lateinit var adaptador: AdaptadorLibros
    private lateinit var linearCargando: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_libro_lista, container, false)
        //return inflater.inflate(R.layout.fragment_libro_lista, container, false)

        linearCargando = vista.findViewById(R.id.linearCargando)
        recyclerView = vista.findViewById(R.id.recyclerView)
        //Definimos el arreglo que vamos a llenar cuando obtengamos los objetos de Firebase
        listaLibros = arrayListOf()
        //Definimos que va a cargar los datos del arreglo librosLista
        adaptador = AdaptadorLibros(this, listaLibros)
        //El recyclerview mostrará dos libros de manera vertical
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adaptador
        obtenerLibros()

        return vista
    }

    /**Función que obtiene todos los datos (libros) que se encuentran en Firebase y que le pasamos al adaptadorLibros para que
     * rellene el recyclerview con la portada y el nombre de cada libro. Además de implementar la funcionalidad de que va hacer
     * cuando el usuario de click sobre item del recyclerview*/
    private fun obtenerLibros(){
        //Obtenemos todos los datos de la colección Libros
        FirebaseFirestore.getInstance().collection("Libros")
            .get()
            .addOnSuccessListener { documents ->
                //Creamos la animacion de que está cargando los libros
                linearCargando.visibility = View.INVISIBLE

                //Recorremos todos los datos rellenenado el arreglo librosLista
                for (document in documents){
                    //Obtenemos cada objeto y lo pasamos a una variable llamada libro
                    val libro = document.toObject(Libro::class.java)
                    listaLibros.add(libro)
                }
                //Cuando tenemos el arreglo de libros completo, ejecutamos el adaptador para que rellene nuestro recyclerview
                recyclerView.adapter = adaptador
                //Mostramos el recyclerview, pasando por detras la animación de cargar libros
                recyclerView.visibility = View.VISIBLE

                //Cunado el usuario de click en un libro mandamos a llamar a la función mostrarDetalleLibro y le pasamos objeto que se selecciono
                adaptador.setOnClickListener(object : AdaptadorLibros.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        mostrarDetalleLibro(listaLibros[position])
                    }
                })

            }
            .addOnFailureListener {
                Toast.makeText(context,"Error al obtener datos: " +it, Toast.LENGTH_LONG).show()
            }
    }

    /**Función que muestra el fragmento libro_detalle que recibe como parámetro el objeto Libro seleccionado por el
     * usuario y le envia esos datos a la  siguiente vista (reproductor del audiolibro)*/
    fun mostrarDetalleLibro(libro: Libro){
        //Le enviamos los datos a traves de un bundle
        val bundle = bundleOf("ID" to libro.id,
            "Titulo" to libro.titulo,
            "Imagen" to libro.imagen,
            "Resumen" to libro.resumen,
            "Lanzamiento" to libro.lanzamiento)

        val fragmentoDetalle = LibroDetalleFragment()
        fragmentoDetalle.arguments = bundle

        //Realizamos la transaccion al fragment LibroDetalle
        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in, //Animación de deslizamiento (derecha a izquierda) que muestra la siguiente vista
                R.anim.fade_out, //Animación para la salida para este fragmento (alpha)
                R.anim.fade_in, //Animación para regresar (deslizamiento de izquierda a derechz) cuando el usuario da click en regresar
                R.anim.slide_out //Animación para desaparecer (alpha) el fragmento de detalle libro
            )
            replace(R.id.contenedor_Fragment, fragmentoDetalle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}
