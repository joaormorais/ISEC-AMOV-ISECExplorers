package com.example.amovtp.data

/**
 * Represents a location with N points of interest
 */
data class Location(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords:Boolean,
    val pointsOfInterest: MutableList<String>,
    val imgs: List<String>,
    val votes: Int,
    val isApproved: Boolean
)

/**
 * Represents a category for points of interest
 */
data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val img: String,
    val votes: Int,
    val isApproved: Boolean
)

/**
 * Represents a point of interest from a location or more, and has a category
 */
data class PointOfInterest(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords:Boolean,
    val locations: String,
    val category: String,
    val imgs: List<String>,
    val votes: Int,
    val isApproved: Boolean
)

class GeoData(/*firestore*/) {

    companion object {
        private var _currentLocationId = 0
        private var _currentCategoryId = 0
        private var _currentPointsOfInterestId = 0
    }

    private val _locations = mutableListOf<Location>()
    private val _pointsOfInterest = mutableListOf<PointOfInterest>()
    private val _categories = mutableListOf<Category>()

    init { //TODO: apagar

        // Criação das instâncias das categorias
        val museumCategory = Category(
            _currentCategoryId++,
            "Museus",
            "Locais dedicados à exposição e preservação de artefatos culturais.",
            "",
            2,
            true
        )
        val monumentsCategory = Category(
            _currentCategoryId++,
            "Monumentos & Locais de Culto",
            "Locais históricos e religiosos notáveis.",
            "",
            2,
            true
        )
        val gardensCategory = Category(
            _currentCategoryId++,
            "Jardins",
            "Áreas verdes planejadas para fins estéticos e de lazer.",
            "",
            2,
            true
        )
        val viewpointsCategory =
            Category(
                _currentCategoryId++,
                "Miradouros",
                "Locais com vistas panorâmicas.",
                "",
                2,
                true
            )

        val restaurantsBarsCategory = Category(
            _currentCategoryId++,
            "Restaurantes & Bares",
            "Locais para refeições e entretenimento.",
            "",
            2,
            true
        )
        val accommodationCategory = Category(
            _currentCategoryId++,
            "Alojamento",
            "Hospedagem e acomodações para estadias temporárias.",
            "",
            2,
            true
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
            37.0194, 38.5250, 39.7443, 40.6575, 41.6946,
            39.2364, 38.5719, 40.5371, 39.2904, 38.0156,
            41.8089, 41.2974, 39.8234
        )

        val longitudes = doubleArrayOf(
            -9.1393, -8.6291, -8.6215, -8.6455, -8.4293,
            -7.9306, -8.8909, -8.8072, -7.9142, -8.8304,
            -8.6850, -7.9135, -7.2656, -7.4283, -7.8652,
            -6.7579, -7.7463, -7.4914
        )

        for (i in locationNames.indices) {
            _locations.add(
                Location(
                    _currentLocationId++,
                    locationNames[i],
                    locationDescriptions[i],
                    latitudes[i],
                    longitudes[i],
                    false,
                    mutableListOf(),
                    mutableListOf(),
                    2,
                    true
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
                location.lat,
                location.long,
                false,
               location.name,
                _categories.random().name,
                mutableListOf(),
                2,
                true
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

    fun addLocation(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        imgs: List<String>
    ) {

        _locations.add(
            Location(
                _currentLocationId++,
                name,
                description,
                lat,
                long,
                isManualCoords,
                mutableListOf(),
                imgs,
                0,
                false
            )
        )

    }

    fun addPointOfInterest(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        locations: String,
        category: String,
        imgs: List<String>
    ) {

        _pointsOfInterest.add(
            PointOfInterest(
                _currentPointsOfInterestId++,
                name,
                description,
                lat,
                long,
                isManualCoords,
                locations,
                category,
                imgs,
                0,
                false
            )
        )

    }

    fun addCategory(
        name: String,
        description: String,
        img: String,
    ) {

        _categories.add(Category(_currentCategoryId++, name, description, img, 0, false))

    }

}