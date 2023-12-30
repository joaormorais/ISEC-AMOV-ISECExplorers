package com.example.amovtp.ui.screens

enum class Screens(val path: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("PointsOfInterest?itemName={itemId}"),
    CATEGORIES("Categories"),
    ADD_LOCATION("AddLocation"),
    ADD_POINT_OF_INTEREST("AddPointOfInterest"),
    ADD_CATEGORY("AddCategory"),
    EDIT_LOCATIONS("EditLocation?itemName={itemId}"),
    EDIT_POINT_OF_INTEREST("EditPointOfInterest?itemName={itemId}"),
    EDIT_CATEGORY("EditCategory?itemName={itemId}"),
    CREDITS("Credits");

    val route: String
        get() = this.path
}
