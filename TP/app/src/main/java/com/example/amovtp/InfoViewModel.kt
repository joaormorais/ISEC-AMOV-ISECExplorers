package com.example.amovtp

import androidx.lifecycle.ViewModel

/**
 * Represents a category of locations, that is going to have a name, and N locations
 */
data class Category(
    val name: String,
    val description: String,
    val locations: MutableList<Location> = mutableListOf()
) //TODO: receber uma imagem

/**
 * Represents a location from a category, that is going to have a name, and N points of interest
 */
data class Location(
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val pointsOfInterest: MutableList<PointsOfInterest> = mutableListOf()
) //TODO: receber um array de imagens

/**
 * Represents a point of interest from a location, that is going to have a name
 */
data class PointsOfInterest(
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
) //TODO: receber um array de imagens

class InfoViewModel : ViewModel() {

    private val _categories = mutableListOf<Category>()
    val categories: List<Category>
        get() = _categories


    //TODO: funções para subtrair categorias, localizações, e locais de interesse

    fun addCategory(
        categoryName: String,
        categoryDescription: String
        //TODO: receber uma imagem
    ): Boolean {

        // check if the new category already exists
        for (i in _categories)
            if (i.name == categoryName)
                return false

        // add category
        _categories.add(Category(categoryName, categoryDescription)) //TODO: adicionar a imagem

        return true

    }

    fun addLocation(
        categoryName: String,
        locationName: String,
        locationDescription: String,
        latitude: Double,
        longitude: Double
        //TODO: recebe pelo menos uma imagem
    ): Boolean {

        for (i in _categories)
            if (i.name == categoryName)
                for (m in i.locations) {
                    if (m.name == locationName)
                        return false

                    i.locations.add(
                        Location(
                            locationName,
                            locationDescription,
                            latitude, longitude
                        )
                    ) //TODO: adicionar imagem
                    return true
                }

        return false

    }

    fun addPointOfInterest(
        categoryName: String,
        locationName: String,
        pointOfInterestName: String,
        pointOfInterestDescription: String,
        latitude: Double,
        longitude: Double
        //TODO: recebe pelo menos uma imagem
    ): Boolean {

        for (i in _categories)
            if (i.name == categoryName)
                for (m in i.locations) {
                    if (m.name == locationName)
                        for (k in m.pointsOfInterest) {
                            if (k.name == pointOfInterestName)
                                return false

                            m.pointsOfInterest.add(
                                PointsOfInterest(
                                    pointOfInterestName,
                                    pointOfInterestDescription, latitude, longitude
                                )
                            ) //TODO: adicionar imagem
                        }

                    return true
                }

        return false

    }

}