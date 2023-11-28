package com.example.amovtp

enum class Screens(val display: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("Points of Interest"),
    ADD("Add"), // temp
    CLASSIFY("Classify"); // temp

    val route: String // colocar privada aqui neste?
        get() = this.toString()
}

// teste teste teste