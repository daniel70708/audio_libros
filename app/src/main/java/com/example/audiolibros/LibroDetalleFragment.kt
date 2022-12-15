package com.example.audiolibros

import android.content.ContentValues.TAG
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class LibroDetalleFragment : Fragment() {
    private lateinit var ID_documento: String
    private lateinit var listaAudio: ArrayList<Audio>

    private lateinit var botonRegresarFragment: ImageButton
    private lateinit var portadaLibro: ImageView
    private lateinit var tiempoRestante: TextView

    private lateinit var nombreLibro: TextView
    private lateinit var tiempoInicio: TextView
    private lateinit var tiempoFinal: TextView

    private lateinit var botonRegresarAudio: ImageButton
    private lateinit var botonRegresar30: ImageButton
    private lateinit var botonIniciar: ImageButton
    private lateinit var botonAdelantar30: ImageButton
    private lateinit var botonSiguienteAudio: ImageButton

    private lateinit var resumenLibro: TextView
    private lateinit var fechaLanzamiento: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listaAudio = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_libro_detalle, container, false)
        botonRegresarFragment = vista.findViewById(R.id.botonRegresar)
        tiempoRestante = vista.findViewById(R.id.tiempo_restante)
        tiempoInicio = vista.findViewById(R.id.tiempo_inicio)
        tiempoFinal = vista.findViewById(R.id.tiempo_final)
        botonIniciar = vista.findViewById(R.id.botonReproducir)

        nombreLibro = vista.findViewById(R.id.titulo)
        portadaLibro = vista.findViewById(R.id.portada)
        resumenLibro = vista.findViewById(R.id.resumen)
        fechaLanzamiento = vista.findViewById(R.id.fechaLanzamiento)
        obtenerBundle()
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        limpiarTextos()

        botonIniciar.setOnClickListener {
            obtenerAudios()
        }

        botonRegresarFragment.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<LibroListaFragment>(R.id.contenedor_Fragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    fun limpiarTextos(){
        tiempoRestante.text = ""
        tiempoInicio.text = "0.0"
        tiempoFinal.text = "0.0"
    }

    fun obtenerAudios(){

        FirebaseFirestore.getInstance().collection("Libros").document(ID_documento)
            .collection(COLECCION_AUDIOS).get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val audio = document.toObject(Audio::class.java)
                    listaAudio.add(audio)
                }
            }
            .addOnFailureListener { exeption ->
                Log.w(TAG, "Error getting documents: ", exeption)
            }

    }

    fun obtenerBundle(){
        if (arguments != null) {
            ID_documento = requireArguments().getString("ID").toString()
            val tituloBundle = requireArguments().getString("Titulo")
            val imagenBundle = requireArguments().getString("Imagen")
            val resumenBundle = requireArguments().getString("Resumen")
            val lanzamientoBundle = requireArguments().getString("Lanzamiento")

            nombreLibro.text = tituloBundle
            Glide.with(requireContext())
                .load(imagenBundle)
                .into(portadaLibro)
            resumenLibro.text = resumenBundle
            fechaLanzamiento.text = lanzamientoBundle
        }
    }

    companion object {
        private const val COLECCION_AUDIOS = "Audios"
    }
}

