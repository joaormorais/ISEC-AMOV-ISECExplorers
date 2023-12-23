package com.example.amovtp.data

import com.example.amovtp.utils.fb.FStorageUtil

/**
 * Represents a location with N points of interest
 */
data class Location(
    val id: Long,
    val userId: String,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords: Boolean,
    val pointsOfInterest: List<String>,
    val imgs: List<String>,
    var votes: Long,
    var isApproved: Boolean
)

/**
 * Represents a category for points of interest
 */
data class Category(
    val id: Long,
    val userId: String,
    val name: String,
    val description: String,
    val img: String,
    var votes: Long,
    var isApproved: Boolean
)

/**
 * Represents a point of interest from N locations, that has a category
 */
data class PointOfInterest(
    val id: Long,
    val userId: String,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords: Boolean,
    val locations: List<String>,
    val category: String,
    val imgs: List<String>,
    var classification: Double,
    var nClassifications: Long,
    var votes: Long,
    var isApproved: Boolean
)

/**
 * Locations, Points of interest and categories
 */
class GeoData(private val fStorageUtil: FStorageUtil) {

    companion object {
        private var _currentLocationId: Long = 0L
        private var _currentCategoryId: Long = 0L
        private var _currentPointsOfInterestId: Long = 0L
    }

    private val _locations = mutableListOf<Location>()
    private val _pointsOfInterest = mutableListOf<PointOfInterest>()
    private val _categories = mutableListOf<Category>()

    init {

        fStorageUtil.startObserverGeoData(
            onNewLocations = { locMapList ->

                /*if(locMapList.isNotEmpty())
                for (locMap in locMapList)
                    for (i in locMap) {
                        _locations.add(
                            Location(
                                id = locMap["id"] as Long,
                                userId = locMap["userId"] as String,
                                name = locMap["name"] as String,
                                description = locMap["description"] as String,
                                lat = locMap["lat"] as Double,
                                long = locMap["long"] as Double,
                                isManualCoords = locMap["isManualCoords"] as Boolean,
                                pointsOfInterest = locMap["pointsOfInterest"] as List<String>,
                                imgs = locMap["imgs"] as List<String>,
                                votes = locMap["votes"] as Long,
                                isApproved = locMap["isApproved"] as Boolean
                            )
                        )
                    }
*/
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
        userId: String,
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
                userId,
                name,
                description,
                lat,
                long,
                isManualCoords,
                mutableListOf(),
                imgs,
                0L,
                false
            )
        ) {}


    }

    fun addPointOfInterest(
        userId: String,
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
                userId,
                name,
                description,
                lat,
                long,
                isManualCoords,
                locations,
                category,
                imgs,
                0.0,
                0L,
                0L,
                false
            )
        ) {}

    }

    fun addCategory(
        userId: String,
        name: String,
        description: String,
        img: String,
    ) {

        fStorageUtil.addCategoryToFirestore(
            Category(
                _currentCategoryId++,
                userId,
                name,
                description,
                img,
                0L,
                false
            )
        ) {}

    }

    /* ------------------------  Add, remove, and edit info (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun voteForApprovalLocation(id: Long) {
        _locations.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalLocation(id: Long) {
        _locations.find { it.id == id }?.apply { votes-- }
    }

    fun approveLocation(id: Long) {
        _locations.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapproveLocation(id: Long) {
        _locations.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun voteForApprovalPointOfInterest(id: Long) {
        _pointsOfInterest.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalPointOfInterest(id: Long) {
        _pointsOfInterest.find { it.id == id }?.apply { votes-- }
    }

    fun approvePointOfInterest(id: Long) {
        _pointsOfInterest.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapprovePointOfInterest(id: Long) {
        _pointsOfInterest.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Category approval (Start) ------------------------ */
    fun voteForApprovalCategory(id: Long) {
        _categories.find { it.id == id }?.apply { votes++ }
    }

    fun removeVoteForApprovalCategory(id: Long) {
        _categories.find { it.id == id }?.apply { votes-- }
    }

    fun approveCategory(id: Long) {
        _categories.find { it.id == id }?.apply { isApproved = true }
    }

    fun disapproveCategory(id: Long) {
        _categories.find { it.id == id }?.apply { isApproved = false }
    }
    /* ------------------------  Category approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addClassificationToPoint(pointOfInterestID: Long, classification: Double) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.classification =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.classification!! + classification
    }

    fun incrementNumberOfClassifications(pointOfInterestID: Long) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications!! + 1
    }

    fun removeClassificationToPoint(pointOfInterestID: Long, classification: Double) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.classification =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.classification!! - classification
    }

    fun decrementNumberOfClassifications(pointOfInterestID: Long) {
        _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications =
            _pointsOfInterest.find { it.id == pointOfInterestID }?.nClassifications!! - 1
    }
    /* ------------------------  Point classification (End) ------------------------ */
}