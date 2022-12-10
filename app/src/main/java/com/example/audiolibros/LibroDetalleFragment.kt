package com.example.audiolibros

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

class LibroDetalleFragment : Fragment() {
    private lateinit var tituloLibro: TextView
    private lateinit var portadaLibro: ImageView
    private lateinit var botonRegresar: ImageButton
    private lateinit var tiempoRestante: TextView
    private lateinit var tiempoInicio: TextView
    private lateinit var tiempoFinal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_libro_detalle, container, false)
        botonRegresar = vista.findViewById(R.id.botonRegresar)
        tiempoRestante = vista.findViewById(R.id.tiempo_restante)
        tiempoInicio = vista.findViewById(R.id.tiempo_inicio)
        tiempoFinal = vista.findViewById(R.id.tiempo_final)

        tituloLibro = vista.findViewById(R.id.titulo)
        portadaLibro = vista.findViewById(R.id.portada)


        if (arguments != null) {
            val titulo = requireArguments().getString("Titulo")
            val imagen = requireArguments().getString("Imagen")
            tituloLibro.text = titulo
            Glide.with(requireContext())
                .load(imagen)
                .into(portadaLibro)
        }


        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        limpiarTextos()

        botonRegresar.setOnClickListener {
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
}

