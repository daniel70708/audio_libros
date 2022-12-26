package com.example.audiolibros

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.*
import com.bumptech.glide.Glide
import com.example.audiolibros.databinding.FragmentLibroDetalleBinding
import com.google.firebase.firestore.FirebaseFirestore

class LibroDetalleFragment : Fragment(){

    private var _binding: FragmentLibroDetalleBinding? = null
    private val binding get() = _binding!!

    private lateinit var reproductorAudio: ReproductorMediaPlayer
    private lateinit var ID_libro: String
    private lateinit var listaAudios: ArrayList<Audio>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listaAudios = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLibroDetalleBinding.inflate(inflater, container, false)

        val view = binding.root

        obtenerBundle()
        obtenerAudiosFirebase()
        reproductorAudio = ReproductorMediaPlayer(listaAudios)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        reiniciarInfoAudio()

        binding.includeAudio.botonReproducirAudio.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying()){
                    binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.reproducir_audio)
                    //reproducir.setBackgroundResource(R.drawable.reproducir_audio)
                    reproductorAudio.pausarAudio()
                }else{
                    binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                    //reproducir.setBackgroundResource(R.drawable.pausar_audio)
                    reproductorAudio.reproducirAudio()
                }
            }else{
                binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                //reproducir.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()
                binding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
                //nombreAudio?.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
            }
        }

        binding.includeAudio.botonAudioSiguiente.setOnClickListener {
            binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
            //reproducir.setBackgroundResource(R.drawable.pausar_audio)
            reproductorAudio.reproducirAudioSiguiente()
            binding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
            //nombreAudio?.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
        }

        binding.includeAudio.botonAudioAnterior.setOnClickListener {
            binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
            //reproducir.setBackgroundResource(R.drawable.pausar_audio)
            reproductorAudio.reproducirAudioAnterior()
            //nombreAudio?.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
            binding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
        }

        binding.includeAudio.botonAdelantar10segundos.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying() == false) binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                    // reproducir.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.adelantar30segundos()
            }else{
                binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                //reproducir.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()

            }
        }

        binding.includeAudio.botonRegresar10segundos.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying() == false) binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                    // reproducir.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.retrasar30segundos()
            }else{
                binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                //reproducir.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()
            }
        }

        binding.includePortada.botonRegresarLista.setOnClickListener {
            reproductorAudio.destuirAudio()
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
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    */


    /**Obtenemos los datos del libro que el usuario selecciono en el fragment anterior (ID, nombre, resumen y fecha de lanzamiento del libro)
     * y los asignamos a este fragment para que el usuario tenga más información del libro y eventualmente reproducir el audio libro*/
    private fun obtenerBundle(){

        if (arguments != null) {
            //Asignamos los valores que recibimos de la vista anterior a las variables que referenciamos anteriormente
            ID_libro = requireArguments().getString("ID").toString()
            binding.includeAudio.tituloLibro.text =requireArguments().getString("Titulo")
            val imagenBundle = requireArguments().getString("Imagen")
            binding.includeResumen.resumen.text = requireArguments().getString("Resumen")
            binding.includeResumen.fechaLanzamiento.text = requireArguments().getString("Lanzamiento")
            /*nombreLibro.text = requireArguments().getString("Titulo")
            val imagenBundle = requireArguments().getString("Imagen")
            resumenLibro.text = requireArguments().getString("Resumen")
            fechaLanzamiento.text = requireArguments().getString("Lanzamiento")*/

            Glide.with(requireContext())
                .load(imagenBundle)
                .into(binding.includePortada.portadaLibro)
        }else{
            Toast.makeText(context,"Error al obtener los datos del libro", Toast.LENGTH_LONG).show()
        }
    }

    /**Función en donde obtenemos todos los audios del libro en Firestore, pasando como parámetro el ID del libro y accediendo a la
     * colección de Audio que posee cada libro */
    private fun obtenerAudiosFirebase(){
        /*Cada libro tiene una colección que se llama Audios en donde se encuentran almacenados todos los capítulos del libro (nombre y ruta del audio)*/
        FirebaseFirestore.getInstance().collection("Libros").document(ID_libro).collection(COLECCION_AUDIOS).orderBy("ID_audio")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val audio = document.toObject(Audio::class.java)
                    //Pasamos todos los objetos a un arreglo
                    listaAudios.add(audio)
                }
            }
            .addOnFailureListener { exeption ->
                Log.w(TAG, "Error getting documents: ", exeption)
            }
    }

    fun reiniciarInfoAudio(){

        binding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.reproducir_audio)
        binding.includePortada.nombreAudio.text = ""
        binding.includeAudio.duracionActual.text = "00:00"
        binding.includeAudio.duracionAudio.text = "00:00"
    }

    fun insertarInfoAudios(cadena: String){
        binding.includeAudio.duracionAudio.text = cadena
    }

    companion object {
        private const val COLECCION_AUDIOS = "Audios"
    }
}



