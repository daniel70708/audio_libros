package com.example.audiolibros

data class Libro(
    val titulo: String,
    val imagen: String
    ){
    constructor(): this(
        "",
        "")
}
