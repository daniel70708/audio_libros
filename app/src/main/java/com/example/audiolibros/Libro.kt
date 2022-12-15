package com.example.audiolibros

data class Libro(
    val id: String,
    val titulo: String,
    val imagen: String,
    val resumen: String,
    val lanzamiento: String
    ){
    constructor(): this(
         "",
        "",
        "",
        "",
        "")
}
