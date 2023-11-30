package com.example.amovtp.ui.viewmodels

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
    val location: Location, //TODO: transformar num array de localizações (um ponto poode ter por exemplo Coimbra e Montemor-o-Velho)
    val category: Category
) //TODO: receber um array de imagens + classificação


class PointsOfInterestViewModel : ViewModel() {

    //TODO: ir buscar informação ao GeoInfoRepository

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


    /*fun removePointOfInterest(
        pointOfInterestName: String
    ): Boolean {

        for (i in _pointsOfInterest)
            if (i.name == pointOfInterestName) {
                _pointsOfInterest.remove(i)
                return true
            }

        return false
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

    }*/

}