package com.example.audiolibros

data class Libro(
    val titulo: String,
    val imagen: String,
    val resumen: String,
    val lanzamiento: String
    ){
    constructor(): this(
        "",
        "",
        "",
        "")
}
