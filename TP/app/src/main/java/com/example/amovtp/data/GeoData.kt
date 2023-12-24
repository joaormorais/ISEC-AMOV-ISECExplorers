package com.example.amovtp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.amovtp.services.FStorageService

/**
 * Represents a location with N points of interest
 */
data class Location(
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

class GeoData(private val fStorageService: FStorageService) {

    private val _locations = mutableStateOf(emptyList<Location>())
    private val _pointsOfInterest = mutableStateOf(emptyList<PointOfInterest>())
    private val _categories = mutableStateOf(emptyList<Category>())

    val locations: MutableState<List<Location>>
        get() = _locations

    val pointsOfInterest: MutableState<List<PointOfInterest>>
        get() = _pointsOfInterest

    val categories: MutableState<List<Category>>
        get() = _categories


    init {

        fStorageService.startObserverGeoData(
            onNewLocations = { locMapList ->

                if (locMapList.isNotEmpty()) {
                    val newLocations = locMapList.map { i ->
                        Location(
                            userId = i["userID"] as String,
                            name = i["name"] as String,
                            description = i["description"] as String,
                            lat = i["lat"] as Double,
                            long = i["long"] as Double,
                            isManualCoords = i["isManualCoords"] as Boolean,
                            pointsOfInterest = i["pointsOfInterest"] as List<String>,
                            imgs = i["imgs"] as List<String>,
                            votes = i["votes"] as Long,
                            isApproved = i["isApproved"] as Boolean
                        )
                    }
                    _locations.value = newLocations
                }

            },
            onNewPointsOfInterest = { locPointsList ->

                if (locPointsList.isNotEmpty()) {
                    val newPointsOfInterest = locPointsList.map { i ->
                        PointOfInterest(
                            userId = i["userID"] as String,
                            name = i["name"] as String,
                            description = i["description"] as String,
                            lat = i["lat"] as Double,
                            long = i["long"] as Double,
                            isManualCoords = i["isManualCoords"] as Boolean,
                            locations = i["locations"] as List<String>,
                            category=i["category"] as String,
                            imgs = i["imgs"] as List<String>,
                            classification = i["classification"] as Double,
                            nClassifications = i["nClassifications"] as Long,
                            votes = i["votes"] as Long,
                            isApproved = i["isApproved"] as Boolean
                        )
                    }
                    _pointsOfInterest.value = newPointsOfInterest
                }

            },
            onNewCategories = { locCatsList ->

                if(locCatsList.isNotEmpty()){
                    val newCategories = locCatsList.map { i ->
                        Category(
                            userId = i["userID"] as String,
                            name = i["name"] as String,
                            description = i["description"] as String,
                            img = i["img"] as String,
                            votes = i["votes"] as Long,
                            isApproved = i["isApproved"] as Boolean
                        )
                    }
                    _categories.value = newCategories
                }

            }
        )

    }

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

        fStorageService.addLocationToFirestore(
            Location(
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

        fStorageService.addPointOfInterestToFirestore(
            PointOfInterest(
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

        fStorageService.addCategoryToFirestore(
            Category(
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
    fun voteForApprovalLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { votes++ }
    }

    fun removeVoteForApprovalLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { votes-- }
    }

    fun approveLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { isApproved = true }
    }

    fun disapproveLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { isApproved = false }
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun voteForApprovalPointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { votes++ }
    }

    fun removeVoteForApprovalPointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { votes-- }
    }

    fun approvePointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }
            ?.apply { isApproved = true }
    }

    fun disapprovePointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }
            ?.apply { isApproved = false }
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Category approval (Start) ------------------------ */
    fun voteForApprovalCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { votes++ }
    }

    fun removeVoteForApprovalCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { votes-- }
    }

    fun approveCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { isApproved = true }
    }

    fun disapproveCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { isApproved = false }
    }
    /* ------------------------  Category approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addClassificationToPoint(pointOfInterestName: String, classification: Double) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.classification =
            _pointsOfInterest.value.find { it.name == pointOfInterestName }?.classification!! + classification
    }

    fun incrementNumberOfClassifications(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.nClassifications =
            _pointsOfInterest.value.find { it.name == pointOfInterestName }?.nClassifications!! + 1
    }

    fun removeClassificationToPoint(pointOfInterestName: String, classification: Double) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.classification =
            _pointsOfInterest.value.find { it.name == pointOfInterestName }?.classification!! - classification
    }

    fun decrementNumberOfClassifications(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.nClassifications =
            _pointsOfInterest.value.find { it.name == pointOfInterestName }?.nClassifications!! - 1
    }
    /* ------------------------  Point classification (End) ------------------------ */
}