package com.example.audiolibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Cargamos el fragment que contrendrá las portadas de los libros
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LibroListaFragment>(R.id.contenedor_Fragment)
        }

    }

}