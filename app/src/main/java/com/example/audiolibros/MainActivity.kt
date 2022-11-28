package com.example.audiolibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listaLibro = mutableListOf(
            Libro("Bienvenidos a la casa de la muerte", R.drawable.ic_goosebumps, "R.L. Stine", 2),
            Libro("No bajes al sotano", R.drawable.ic_goosebumps, "R.L. Stine", 3),
            Libro("La noche del muñeco viviente", R.drawable.ic_goosebumps, "R.L. Stine", 3)
        )

        val adaptador = AdaptadorLibros(listaLibro)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adaptador
        recyclerView.layoutManager = GridLayoutManager(this, 2,
        LinearLayoutManager.HORIZONTAL, false)

    }
}