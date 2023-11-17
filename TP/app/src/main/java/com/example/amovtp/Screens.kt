package com.example.amovtp

enum class Screens(val display: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    HOME("Home"),
    ADD("Add"),
    CLASSIFY("Classify");

    val route: String // colocar privada aqui neste?
        get() = this.toString()
}