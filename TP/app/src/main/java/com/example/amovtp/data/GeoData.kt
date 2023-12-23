package com.example.amovtp.data

import com.example.amovtp.utils.fb.FStorageUtil

/**
 * Represents a location with N points of interest
 */
data class Location(
    val id: Int,
    val userId:String,
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
    val userId:String,
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
    val userId:String,
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

    init {

        fStorageUtil.startObserverGeoData(
            onNewLocations = { locMapList ->

                for (locMap in locMapList)
                    for (i in locMap) {
                        _locations.add(
                            Location(
                                id = locMap["id"] as Int,
                                userId = locMap["userId"] as String,
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
        userId:String?,
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
                userId!!,
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
        userId:String?,
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
                userId!!,
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
        userId:String?,
        description: String,
        img: String,
    ) {

        fStorageUtil.addCategoryToFirestore(
            Category(
                _currentCategoryId++,
                name,
                userId!!,
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