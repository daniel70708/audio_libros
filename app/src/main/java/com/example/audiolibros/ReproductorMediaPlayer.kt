package com.example.audiolibros

import android.media.AudioManager
import android.media.MediaPlayer

class ReproductorMediaPlayer (
    private val listaAudios: ArrayList<Audio>):
    MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var audio: MediaPlayer? = null
    private var indiceActual: Int = 0

    override fun onPrepared(p0: MediaPlayer?) {
       reproducirAudio()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        reproducirAudioSiguiente()
    }

    fun estadoAudio(): Boolean = audio != null

    fun iniciarAudio() {
        audio?.reset()
        audio = MediaPlayer()

        val url = listaAudios[obtenerAudioActual()].Url_audio
        audio?.apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(url)
            setOnPreparedListener(this@ReproductorMediaPlayer)
            prepareAsync()
        }
    }

    fun reproducirAudio() = audio?.start()

    fun pausarAudio() = audio?.pause()

    fun isPlaying(): Boolean = audio?.isPlaying == true

    fun reproducirAudioSiguiente(){
        if (isPlaying()) pausarAudio()
        indiceActual ++
        if(obtenerAudioActual() >= listaAudios.size) indiceActual = 0
        iniciarAudio()
    }

    fun reproducirAudioAnterior(){
        if (isPlaying()) pausarAudio()
        indiceActual --
        if(obtenerAudioActual() < 0) indiceActual = listaAudios.size - 1
        iniciarAudio()
    }

    fun obtenerAudioActual(): Int = indiceActual

    fun adelantar30segundos(){
        if (isPlaying()) pausarAudio()
        audio?.currentPosition?.let { audio?.seekTo(it.plus(10000)) }
        reproducirAudio()
    }

    fun retrasar30segundos(){
        if (isPlaying()) pausarAudio()
        audio?.currentPosition?.let { audio?.seekTo(it.minus(10000)) }
        reproducirAudio()
    }

    fun obtenerDuracion(): String{
        var cadena = ""
        if(audio != null) {

            val segundos = audio!!.duration / 1000

            if(segundos >= 60){
                val minutes = audio!!.duration / 1000 / 60
                val seconds = audio!!.duration  / 1000 % 60

                if (minutes <= 9)  cadena = "0" + "$minutes" + ":"
                else cadena = "$minutes" + ":"

                if (seconds <= 9) cadena += "0" +"$seconds"
                else cadena += "$seconds"

                return cadena

            }else return "00:" + "$segundos"

        }else return "00:00"

    }

    fun obtenerDuracionMilisegundos(): Int{
        if (audio != null) return audio!!.duration
        else return 0
    }

    fun moverAudio(){}

    fun destuirAudio(){
        if(isPlaying()) pausarAudio()
        listaAudios.clear()
        audio?.release()
        audio = null
    }

}