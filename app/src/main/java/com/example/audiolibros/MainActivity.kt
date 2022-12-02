package com.example.audiolibros

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout.TRUE
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Boolean.TRUE
import java.util.jar.Pack200.Packer.TRUE


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var librosLista:ArrayList<Libro>
    private lateinit var adaptador: AdaptadorLibros
    private lateinit var linearCargando: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearCargando = findViewById(R.id.linearCargando)
        recyclerView = findViewById(R.id.recyclerView)
        librosLista = arrayListOf()
        adaptador = AdaptadorLibros(this, librosLista)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adaptador

        obtenerDatos()

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
                Log.e(TAG, "Error al obtener datos ")
            }
    }
}