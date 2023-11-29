package com.example.amovtp.ui.screens

enum class Screens(val display: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("Points of interest"),
    ADD_LOCATION("Add location"),
    ADD_POINT_OF_INTEREST("Add point of interest"),
    ADD_CATEGORY("Add category");

    val route: String
        get() = this.toString()
}