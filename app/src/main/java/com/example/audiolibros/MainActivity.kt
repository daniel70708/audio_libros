package com.example.audiolibros

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast.makeText as makeText1

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var librosLista:ArrayList<Libro>
    private lateinit var adaptador: AdaptadorLibros

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener datos ")
            }
    }
}