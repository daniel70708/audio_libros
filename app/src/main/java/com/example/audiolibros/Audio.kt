package com.example.audiolibros

data class Audio(
    val nombreAudio: String,
    val cadenaAudio: String
){
    constructor():this(
        "",
        ""
    )
}
