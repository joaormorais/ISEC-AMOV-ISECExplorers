package com.example.amovtp.ui.screens

enum class Screens(val path: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    LOCATIONS("Locations"),
    POINTS_OF_INTEREST("PointsOfInterest?itemName={itemName}"),
    CATEGORIES("Categories"),
    ADD_LOCATION("AddLocation"),
    ADD_POINT_OF_INTEREST("AddPointOfInterest"),
    ADD_CATEGORY("AddCategory"),
    EDIT_LOCATIONS("EditLocation?itemId={itemId}"),
    EDIT_POINT_OF_INTEREST("EditPointOfInterest?itemId={itemId}"),
    EDIT_CATEGORY("EditCategory?itemId={itemId}"),
    CREDITS("Credits");

    val route: String
        get() = this.path
}
