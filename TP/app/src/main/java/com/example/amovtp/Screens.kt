package com.example.amovtp

enum class Screens(val display: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    HOME("Home"),
    ADD("Add"),
    CLASSIFY("Classify");

    val route: String // colocar privada aqui?
        get() = this.toString()
}