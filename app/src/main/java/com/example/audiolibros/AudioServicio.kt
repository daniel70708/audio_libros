package com.example.audiolibros

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder

class AudioServicio: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private var audio: MediaPlayer? = null
    private lateinit var listaAudios: List<Audio>

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO("Not yet implemented")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onPrepared(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onCompletion(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }

}