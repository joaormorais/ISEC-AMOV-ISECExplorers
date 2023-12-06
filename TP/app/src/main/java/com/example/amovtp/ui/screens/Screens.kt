package com.example.amovtp.ui.screens

//TODO: transformar este enum numa lista de paths?

enum class Screens(val display: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("PointsOfInterest"),
    ADD_LOCATION("AddLocation"),
    ADD_POINT_OF_INTEREST("AddPointOfInterest"),
    ADD_CATEGORY("AddCategory");

    val route: String
        get() = this.toString()
}
