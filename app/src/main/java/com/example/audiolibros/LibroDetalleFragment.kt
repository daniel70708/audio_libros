package com.example.audiolibros

import android.content.ContentValues.TAG
import android.media.AudioManager
import android.media.Image
import android.media.MediaPlayer
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

class LibroDetalleFragment : Fragment(){
    private lateinit var ID_documento: String
    private lateinit var listaAudio: ArrayList<Audio>

    private lateinit var botonRegresarFragment: ImageButton
    private lateinit var tiempoRestante: TextView

    private lateinit var tiempoInicio: TextView
    private lateinit var tiempoFinal: TextView

    private lateinit var botonRegresarAudio: ImageButton
    private lateinit var botonRegresar30: ImageButton
    private lateinit var botonIniciar: ImageButton
    private lateinit var botonAdelantar30: ImageButton
    private lateinit var botonSiguienteAudio: ImageButton


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

        obtenerLibro(vista)
        obtenerAudios()
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        limpiarTextos()

        botonIniciar.setOnClickListener {
            iniciarAudios(0)
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

    /**Obtenemos los datos del libro que el usuario selecciono en el fragment anterior (ID, nombre, resumen y fecha de lanzamiento del libro)
     * y los asignamos a este fragment para que el usuario tenga más información del libro y eventualmente reproducir el audio libro*/
    fun obtenerLibro(vista: View){

        //Creamos la referencia a la vista
        var nombreLibro = vista.findViewById<TextView>(R.id.titulo)
        var portadaLibro = vista.findViewById<ImageView>(R.id.portada)
        var resumenLibro = vista.findViewById<TextView>(R.id.resumen)
        var fechaLanzamiento = vista.findViewById<TextView>(R.id.fechaLanzamiento)

        if (arguments != null) {
            //Asignamos los valores que recibimos de la vista anterior a las variables que referenciamos anteriormente
            ID_documento = requireArguments().getString("ID").toString()
            nombreLibro.text = requireArguments().getString("Titulo")
            val imagenBundle = requireArguments().getString("Imagen")
            resumenLibro.text = requireArguments().getString("Resumen")
            fechaLanzamiento.text = requireArguments().getString("Lanzamiento")
            Log.d("ID", "El ID del libro es: " +ID_documento)

            Glide.with(requireContext())
                .load(imagenBundle)
                .into(portadaLibro)
        }else{
            Toast.makeText(context,"Error al obtener los datos del libro", Toast.LENGTH_LONG).show()
        }
    }

    /**Función en donde obtenemos todos los audios del libro en Firestore, pasando como parámetro el ID del libro y accediendo a la
     * colección de Audio que posee cada libro */
    fun obtenerAudios(){
        /*Cada libro tiene una colección que se llama Audios en donde se encuentran almacenados todos los capítulos del libro (nombre y ruta del audio)*/
        FirebaseFirestore.getInstance().collection("Libros").document(ID_documento).collection(COLECCION_AUDIOS).orderBy("ID_audio")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val audio = document.toObject(Audio::class.java)
                    //Pasamos todos los objetos a un arreglo
                    listaAudio.add(audio)
                    Log.d("ID_audio" ,"El nombre del audio es: " +listaAudio[0].Nombre_audio)
                }
            }
            .addOnFailureListener { exeption ->
                Log.w(TAG, "Error getting documents: ", exeption)
            }
    }

    fun iniciarAudios(capitulo: Int){
        val url = listaAudio[capitulo].Url_audio
            //listaAudio[capitulo].Url_audio// your URL here
        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }

    }

    fun limpiarTextos(){
        tiempoRestante.text = ""
        tiempoInicio.text = "0.0"
        tiempoFinal.text = "0.0"
    }

    companion object {
        private const val COLECCION_AUDIOS = "Audios"
    }
}

