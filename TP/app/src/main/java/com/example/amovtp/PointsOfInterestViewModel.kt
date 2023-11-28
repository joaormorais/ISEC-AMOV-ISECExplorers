package com.example.amovtp

import androidx.lifecycle.ViewModel

/**
 * Represents a category of points of interest, that is going to have a name, and a description
 */
data class Category(
    val id: Int,
    val name: String,
    val description: String
) //TODO: receber uma imagem

/**
 * Represents a point of interest from a location, that is going to have a name, a description, coordinates, and a category
 */
data class PointsOfInterest(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val location: Location,
    val category: Category
) //TODO: receber um array de imagens

class PointsOfInterestViewModel : ViewModel() {

    companion object {
        private var _currentCategoryId = 0
        private var _currentPointsOfInterestId = 0
    }

    private val _pointsOfInterest = mutableListOf<PointsOfInterest>()
    val pointsOfInterest: List<PointsOfInterest>
        get() = _pointsOfInterest

    private val _categories = mutableListOf<Category>()
    val categories: List<Category>
        get() = _categories

    init { // TODO: passar para o ficheiro strings.xml
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
        _categories.add(
            Category(
                _currentCategoryId++,
                categoryName,
                categoryDescription
            )
        ) //TODO: adicionar a imagem

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

    fun addPointOfInterest(
        location: Location,
        category: Category,
        pointOfInterestName: String,
        pointOfInterestDescription: String,
        latitude: Double,
        longitude: Double
        //TODO: recebe pelo menos uma imagem
    ): Boolean {

        for (i in _categories)
            if (i == category) {// if the category exists
                for (k in _pointsOfInterest) {
                    if (k.name == pointOfInterestName)
                        return false
                }

                _pointsOfInterest.add(
                    PointsOfInterest(
                        _currentPointsOfInterestId++,
                        pointOfInterestName,
                        pointOfInterestDescription,
                        latitude,
                        longitude,
                        location,
                        category
                    )
                )
            }





        return false

    }

    fun removePointOfInterest(
        pointOfInterestName: String
    ): Boolean {

        for (i in _pointsOfInterest)
            if (i.name == pointOfInterestName) {
                _pointsOfInterest.remove(i)
                return true
            }

        return false
    }


}