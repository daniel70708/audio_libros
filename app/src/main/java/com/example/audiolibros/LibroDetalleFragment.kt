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

    private lateinit var mBinding: FragmentLibroDetalleBinding

    private lateinit var reproductorAudio: ReproductorMediaPlayer
    private lateinit var ID_libro: String
    private lateinit var listaAudios: ArrayList<Audio>

    init {
        listaAudios = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentLibroDetalleBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        obtenerBundle()
        obtenerAudiosFirebase()
        reproductorAudio = ReproductorMediaPlayer(listaAudios)

        mBinding.includeAudio.botonReproducirAudio.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying()){
                    mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.reproducir_audio)
                    reproductorAudio.pausarAudio()
                }else{
                    mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                    reproductorAudio.reproducirAudio()
                }
            }else{
                mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()
                mBinding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
            }
        }

        mBinding.includeAudio.botonAudioSiguiente.setOnClickListener {
            mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
            reproductorAudio.reproducirAudioSiguiente()
            mBinding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
        }

        mBinding.includeAudio.botonAudioAnterior.setOnClickListener {
            mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
            reproductorAudio.reproducirAudioAnterior()
            mBinding.includePortada.nombreAudio.text = listaAudios[reproductorAudio.obtenerAudioActual()].Nombre_audio
        }

        mBinding.includeAudio.botonAdelantar10segundos.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying() == false) mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.adelantar30segundos()
            }else{
                mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()

            }
        }

        mBinding.includeAudio.botonRegresar10segundos.setOnClickListener {
            val estadoAudio = reproductorAudio.estadoAudio()
            if (estadoAudio){
                if (reproductorAudio.isPlaying() == false) mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.retrasar30segundos()
            }else{
                mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.pausar_audio)
                reproductorAudio.iniciarAudio()
            }
        }

        mBinding.includePortada.botonRegresarLista.setOnClickListener {
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
            mBinding.includeAudio.tituloLibro.text =requireArguments().getString("Titulo")
            val imagenBundle = requireArguments().getString("Imagen")
            mBinding.includeResumen.resumen.text = requireArguments().getString("Resumen")
            mBinding.includeResumen.fechaLanzamiento.text = requireArguments().getString("Lanzamiento")

            Glide.with(requireContext())
                .load(imagenBundle)
                .into(mBinding.includePortada.portadaLibro)

            mBinding.includeAudio.botonReproducirAudio.setBackgroundResource(R.drawable.reproducir_audio)

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

    companion object {
        private const val COLECCION_AUDIOS = "Audios"
    }
}



