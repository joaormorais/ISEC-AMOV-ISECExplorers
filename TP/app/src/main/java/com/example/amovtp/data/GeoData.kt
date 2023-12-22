package com.example.amovtp.data

import com.example.amovtp.utils.fb.FStorageUtil

/**
 * Represents a location with N points of interest
 */
data class Location(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords: Boolean,
    val pointsOfInterest: List<String>,
    val imgs: List<String>,
    var votes: Int,
    var isApproved: Boolean
)

/**
 * Represents a category for points of interest
 */
data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val img: String,
    var votes: Int,
    var isApproved: Boolean
)

/**
 * Represents a point of interest from N locations, that has a category
 */
data class PointOfInterest(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords: Boolean,
    val locations: List<String>,
    val category: String,
    val imgs: List<String>,
    var classification: Int,
    var nClassifications: Int,
    var votes: Int,
    var isApproved: Boolean
)

/**
 * Locations, Points of interest and categories
 */
class GeoData(private val fStorageUtil: FStorageUtil) {

    companion object {
        private var _currentLocationId = 0
        private var _currentCategoryId = 0
        private var _currentPointsOfInterestId = 0
    }

    private val _locations = mutableListOf<Location>()
    private val _pointsOfInterest = mutableListOf<PointOfInterest>()
    private val _categories = mutableListOf<Category>()

    //TODO: para receber informação, fazer o observer no init; ouvir da cloud objetos de Loc, Cat e Ponto

    /*init { //TODO: apagar

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
            1,
            false
        )
        val accommodationCategory = Category(
            _currentCategoryId++,
            "Alojamento",
            "Hospedagem e acomodações para estadias temporárias.",
            "",
            0,
            false
        )

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
                    0,
                    false
                )
            )
        }

        for (location in _locations) {
            val tempPointOfInterest = PointOfInterest(
                _currentPointsOfInterestId++,
                "Ponto de Interesse em ${location.name}",
                "Descrição do ponto de interesse em ${location.name}",
                location.lat,
                location.long,
                false,
                listOf(location.name),
                _categories.random().name,
                mutableListOf(),
                12,
                5,
                1,
                false
            )

            _pointsOfInterest.add(tempPointOfInterest)
            location.pointsOfInterest.add(tempPointOfInterest.name)
        }
    }*/
    init {

        fStorageUtil.startObserverGeoData(
            onNewLocations = { locMapList ->

                for (locMap in locMapList)
                    for (i in locMap) {
                        _locations.add(
                            Location(
                                id = locMap["id"] as Int,
                                name = locMap["name"] as String,
                                description = locMap["description"] as String,
                                lat = locMap["lat"] as Double,
                                long = locMap["long"] as Double,
                                isManualCoords = locMap["isManualCoords"] as Boolean,
                                pointsOfInterest = locMap["pointsOfInterest"] as List<String>,
                                imgs = locMap["imgs"] as List<String>,
                                votes = locMap["votes"] as Int,
                                isApproved = locMap["isApproved"] as Boolean
                            )
                        )
                    }

            },
            onNewPointsOfInterest = { locPoints ->

            },
            onNewCategories = { locCats ->

            }
        )

    }

    val locations: List<Location>
        get() = _locations.toList()

    val pointsOfInterest: List<PointOfInterest>
        get() = _pointsOfInterest.toList()

    val categories: List<Category>
        get() = _categories.toList()

    /* ------------------------  Add, remove, and edit info (Start) ------------------------ */

    fun addLocation(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        imgs: List<String>
    ) {

        fStorageUtil.addLocationToFirestore(
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
        ) {}


    }

    fun addPointOfInterest(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        locations: List<String>,
        category: String,
        imgs: List<String>
    ) {

        fStorageUtil.addPointOfInterestToFirestore(
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
                0,
                0,
                false
            )
        ) {}

    }

    fun addCategory(
        name: String,
        description: String,
        img: String,
    ) {

        fStorageUtil.addCategoryToFirestore(
            Category(
                _currentCategoryId++,
                name,
                description,
                img,
                0,
                false
            )
        ) {}

    }

    /* ------------------------  Add, remove, and edit info (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun voteForApprovalLocation(id: Int) {
        _locations.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalLocation(id: Int) {
        _locations.find { it.id == id }?.apply { votes-- }
    }

    fun approveLocation(id: Int) {
        _locations.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapproveLocation(id: Int) {
        _locations.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun voteForApprovalPointOfInterest(id: Int) {
        _pointsOfInterest.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalPointOfInterest(id: Int) {
        _pointsOfInterest.find { it.id == id }?.apply { votes-- }
    }

    fun approvePointOfInterest(id: Int) {
        _pointsOfInterest.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapprovePointOfInterest(id: Int) {
        _pointsOfInterest.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Category approval (Start) ------------------------ */
    fun voteForApprovalCategory(id: Int) {
        _categories.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalCategory(id: Int) {
        _categories.find { it.id == id }?.apply { votes-- }
    }

    fun approveCategory(id: Int) {
        _categories.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapproveCategory(id: Int) {
        _categories.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Category approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addClassificationToPoint(pointOfInterestID: Int, classification: Int) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.classification =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.classification!! + classification
    }

    fun incrementNumberOfClassifications(pointOfInterestID: Int) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications!! + 1
    }

    fun removeClassificationToPoint(pointOfInterestID: Int, classification: Int) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.classification =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.classification!! - classification
    }

    fun decrementNumberOfClassifications(pointOfInterestID: Int) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications!! - 1
    }
    /* ------------------------  Point classification (End) ------------------------ */
}