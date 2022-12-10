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
    private lateinit var librosLista:ArrayList<Libro>
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
        librosLista = arrayListOf()
        adaptador = AdaptadorLibros(this, librosLista)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adaptador
        obtenerDatos()

        return vista
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
                adaptador.setOnClickListener(object : AdaptadorLibros.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val bundle = bundleOf("Titulo" to librosLista[position].titulo, "Imagen" to librosLista[position].imagen)
                        val fragmento = LibroDetalleFragment()
                        fragmento.arguments = bundle

                        parentFragmentManager.commit {
                            setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                            )
                            replace(R.id.contenedor_Fragment, fragmento)
                            setReorderingAllowed(true)
                            addToBackStack(null)
                        }
                    }
                })

                linearCargando.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE

            }
            .addOnFailureListener {
                Toast.makeText(context,"Error al obtener datos", Toast.LENGTH_LONG).show()
            }
    }
}
