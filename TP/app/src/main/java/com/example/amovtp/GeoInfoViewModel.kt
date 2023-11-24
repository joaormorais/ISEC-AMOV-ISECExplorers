package com.example.amovtp

import androidx.lifecycle.ViewModel

/**
 * Represents a category of points of interest, that is going to have a name, and a description
 */
data class Category(
    val name: String,
    val description: String
) //TODO: receber uma imagem

/**
 * Represents a location, that is going to have a name, a descriptions, coordinates, and N points of interest
 */
data class Location(
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val pointsOfInterest: MutableList<PointsOfInterest> = mutableListOf()
) //TODO: receber um array de imagens

/**
 * Represents a point of interest from a location, that is going to have a name, a description, coordinates, and a category
 */
data class PointsOfInterest(
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val location: String,
    val category: String
) //TODO: receber um array de imagens

/**
 * Viewmodel with the informations abbout the categories, locations and points of interest
 */
class InfoViewModel : ViewModel() {

    private val _categories = mutableListOf<Category>()
    val categories: List<Category>
        get() = _categories

    private val _locations = mutableListOf<Location>()
    val locations: List<Location>
        get() = _locations

    init {
        addCategory(
            "Museus",
            "Explore e descubra a rica herança artística em museus dedicados à arte e à cultura."
        )
        addCategory(
            "Monumentos e locais de culto",
            "Visite monumentos e locais sagrados que contam histórias de devoção e patrimônio."
        )
        addCategory(
            "Jardins",
            "Desfrute da beleza natural em jardins exuberantes, onde a serenidade se encontra com a paisagem."
        )
        addCategory(
            "Miradouros",
            "Contemple vistas panorâmicas deslumbrantes em miradouros estratégicamente posicionados."
        )
        addCategory(
            "Restaurantes e bares",
            "Saboreie experiências gastronómicas únicas em restaurantes e bares que oferecem uma variedade de pratos deliciosos."
        )
        addCategory(
            "Alojamento",
            "Encontre o conforto e a hospitalidade numa variedade de opções de alojamento, incluindo hotéis, pensões e outros estabelecimentos."
        )
    }

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

    fun removeCategory(
        categoryName: String
    ): Boolean {

        // check if the category in fact exists
        for (i in _categories)
            if (i.name == categoryName) {
                _categories.remove(i) // removes the category
                return true
            }

        return false

    }

    fun addLocation(
        locationName: String,
        locationDescription: String,
        latitude: Double,
        longitude: Double
        //TODO: recebe pelo menos uma imagem
    ): Boolean {

        // checks if the location already exists
        for (i in _locations) {
            if (i.name == locationName)
                return false
        }

        // add location
        _locations.add(Location(locationName, locationDescription, latitude, longitude))

        return true

    }

    fun removeLocation(
        locationName: String
    ): Boolean {

        // checks if the location already exists
        for (i in _locations) {
            if (i.name == locationName)
                _locations.remove(i) // removes the location
            return true
        }

        return false

    }

    fun addPointOfInterest(
        locationName: String,
        categoryName: String,
        pointOfInterestName: String,
        pointOfInterestDescription: String,
        latitude: Double,
        longitude: Double
        //TODO: recebe pelo menos uma imagem
    ): Boolean {

        for (i in _categories)
            if (i.name == categoryName) // if the category exists
                for (k in _locations)
                    if (k.name == locationName) { // and if the location exists
                        for (m in k.pointsOfInterest) {
                            if (m.name == pointOfInterestName) // and if the point of interest exists
                                return false
                        }

                        // if the point of interest doesn't exist, we add a new one
                        k.pointsOfInterest.add(
                            PointsOfInterest(
                                pointOfInterestName,
                                pointOfInterestDescription,
                                latitude,
                                longitude,
                                locationName,
                                categoryName
                            )
                        )
                    }

        return false

    }

    fun removePointOfInterest(
        pointOfInterestName: String,
        locationName: String
    ): Boolean {

        for (i in _locations)
            if (i.name == locationName) // if the location exists
                for (k in i.pointsOfInterest)
                    if (k.name == pointOfInterestName) { // and if the point of interest exists
                        i.pointsOfInterest.remove(k) // removes the point of interest
                        return true
                    }

        return false
    }

}