package com.example.amovtp.ui.screens

enum class Screens(val path: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("PointsOfInterest?itemName={itemName}"),
    ADD_LOCATION("AddLocation"),
    ADD_POINT_OF_INTEREST("AddPointOfInterest"),
    ADD_CATEGORY("AddCategory");

    /*val route: String
        get() = this.toString()*/

    val route: String
        get() = this.path
}
