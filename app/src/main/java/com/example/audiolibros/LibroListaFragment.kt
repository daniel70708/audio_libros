package com.example.audiolibros

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LibroListaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LibroListaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var librosLista:ArrayList<Libro>
    private lateinit var adaptador: AdaptadorLibros
    private lateinit var linearCargando: LinearLayout

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        librosLista = arrayListOf()
        adaptador = AdaptadorLibros(this, librosLista)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adaptador
        obtenerDatos()

        return vista
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LibroListaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LibroListaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun obtenerDatos(){
        FirebaseFirestore.getInstance().collection("Libros")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val libro = document.toObject(Libro::class.java)
                    librosLista.add(libro)
                }
                recyclerView.adapter = adaptador
                linearCargando.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE

            }
            .addOnFailureListener {
                Log.e(ContentValues.TAG, "Error al obtener datos ")
            }
    }
}