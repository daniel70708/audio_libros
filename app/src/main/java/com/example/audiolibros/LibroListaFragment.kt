package com.example.audiolibros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audiolibros.databinding.FragmentLibroListaBinding
import com.google.firebase.firestore.FirebaseFirestore


class LibroListaFragment : Fragment() {
    private var _binding: FragmentLibroListaBinding? = null
    private val binding get() = _binding!!

    private lateinit var listaLibros: ArrayList<Libro>
    private lateinit var adaptador: AdaptadorLibros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listaLibros = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLibroListaBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaptador = AdaptadorLibros(this, listaLibros)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adaptador
        obtenerLibros()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listaLibros.clear()
        _binding = null
    }

    /**Función que obtiene todos los datos (libros) que se encuentran en Firebase y que le pasamos al adaptadorLibros para que
     * rellene el recyclerview con la portada y el nombre de cada libro. Además de implementar la funcionalidad de que va hacer
     * cuando el usuario de click sobre item del recyclerview*/
    private fun obtenerLibros(){
        //Obtenemos todos los datos de la colección Libros
        FirebaseFirestore.getInstance().collection("Libros").orderBy("ID_libro")
            .get()
            .addOnSuccessListener { documents ->
                //Creamos la animacion de que está cargando los libros
                binding.linearCargando.visibility = View.INVISIBLE

                //Recorremos todos los datos rellenenado el arreglo librosLista
                for (document in documents){
                    //Obtenemos cada objeto y lo pasamos a una variable llamada libro
                    val libro = document.toObject(Libro::class.java)
                    listaLibros.add(libro)
                }
                //Cuando tenemos el arreglo de libros completo, ejecutamos el adaptador para que rellene nuestro recyclerview
                binding.recyclerView.adapter = adaptador
                //Mostramos el recyclerview, pasando por detras la animación de cargar libros
                binding.recyclerView.visibility = View.VISIBLE

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
        val bundle = bundleOf("ID" to libro.ID_libro,
            "Titulo" to libro.Nombre,
            "Imagen" to libro.Url_imagen,
            "Resumen" to libro.Resumen,
            "Lanzamiento" to libro.Fecha_lanzamiento)

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
