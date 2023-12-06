package com.example.amovtp.data

import androidx.compose.runtime.mutableStateOf

/**
 * Represents a location, that is going to have a name, a descriptions, coordinates, and N points of interest
 */
data class Location(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val pointsOfInterest: MutableList<String> = mutableListOf()
) //TODO: receber um array de imagens + boolean a dizer se é um distrito

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
data class PointOfInterest(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val locations: MutableList<String> = mutableListOf(), //TODO: transformar num array de localizações (um ponto poode ter por exemplo Coimbra e Montemor-o-Velho)
    val category: String
) //TODO: receber um array de imagens + classificação

class GeoData(/*firestore*/) {

    companion object {
        private var _currentLocationId = 0
        private var _currentCategoryId = 0
        private var _currentPointsOfInterestId = 0
    }

    private val _locations = mutableListOf<Location>()
    private val _pointsOfInterest = mutableListOf<PointOfInterest>()
    private val _categories = mutableListOf<Category>()
    private var _pointLocationSearch = ""

    init { //TODO: apagar

        // Criação das instâncias das categorias
        val museumCategory = Category(
            _currentCategoryId++,
            "Museus",
            "Locais dedicados à exposição e preservação de artefatos culturais."
        )
        val monumentsCategory = Category(
            _currentCategoryId++,
            "Monumentos & Locais de Culto",
            "Locais históricos e religiosos notáveis."
        )
        val gardensCategory = Category(
            _currentCategoryId++,
            "Jardins",
            "Áreas verdes planejadas para fins estéticos e de lazer."
        )
        val viewpointsCategory =
            Category(_currentCategoryId++, "Miradouros", "Locais com vistas panorâmicas.")
        val restaurantsBarsCategory = Category(
            _currentCategoryId++,
            "Restaurantes & Bares",
            "Locais para refeições e entretenimento."
        )
        val accommodationCategory = Category(
            _currentCategoryId++,
            "Alojamento",
            "Hospedagem e acomodações para estadias temporárias."
        )

        // Adição das categorias à lista _categories
        _categories.addAll(
            listOf(
                museumCategory,
                monumentsCategory,
                gardensCategory,
                viewpointsCategory,
                restaurantsBarsCategory,
                accommodationCategory
            )
        )

        val locationNames = arrayOf(
            "Lisboa", "Porto", "Braga", "Aveiro", "Coimbra",
            "Faro", "Setubal", "Leiria", "Viseu", "Viana do Castelo",
            "Santarem", "Evora", "Guarda", "Portalegre", "Beja",
            "Bragança", "Vila Real", "Castelo Branco"
        )

        val locationDescriptions =
            Array(locationNames.size) { "Descrição para ${locationNames[it]}" }

        val latitudes = doubleArrayOf(
            38.7223, 41.1579, 41.5331, 40.6443, 40.2105,
            37.0194, 38.5250, 39.7436, 40.6610, 41.6937,
            39.2364, 38.5719, 40.5371, 39.2904, 37.0194,
            41.8089, 41.2974, 39.8234
        )

        val longitudes = doubleArrayOf(
            -9.1393, -8.6291, -8.6215, -8.6455, -8.4293,
            -7.9306, -8.8909, -7.7449, -7.9097, -8.8304,
            -8.6850, -7.9135, -7.2656, -7.4283, -7.8634,
            -6.7579, -8.8071, -7.4914
        )

        for (i in locationNames.indices) {
            _locations.add(
                Location(
                    _currentLocationId++,
                    locationNames[i],
                    locationDescriptions[i],
                    latitudes[i],
                    longitudes[i]
                )
            )
        }

        for (location in _locations) {

            /*val tempLocNames = mutableListOf<String>()
            tempLocNames.add(location.name)*/

            val tempPointOfInterest = PointOfInterest(
                _currentPointsOfInterestId++,
                "Ponto de Interesse em ${location.name}",
                "Descrição do ponto de interesse em ${location.name}",
                333.111,
                444.222,
                mutableListOf(location.name),
                _categories.random().name
            )

            _pointsOfInterest.add(tempPointOfInterest)
            location.pointsOfInterest.add(tempPointOfInterest.name)
        }
    }

    fun getLocations(): List<Location> {
        return _locations.toList()
    }

    fun getPointsOfInterest(): List<PointOfInterest> {
        return _pointsOfInterest.toList()
    }

    fun getCategories(): List<Category> {
        return _categories.toList()
    }

    fun getPointLocationSearch(): String {
        return _pointLocationSearch
    }

    fun setPointLocationSearch(newValue: String) {
        _pointLocationSearch = newValue
    }

}

/*

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
        _locations.add(
            Location(
                _currentLocationId++,
                locationName,
                locationDescription,
                latitude,
                longitude
            )
        )

        return true

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

*/