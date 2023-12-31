package com.example.amovtp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.amovtp.services.FirebaseGeoDataService

/**
 * Represents a location with N points of interest
 */
data class Location(
    var id: String,
    val userId: String,
    var name: String,
    var description: String,
    var lat: Double,
    var long: Double,
    var isManualCoords: Boolean,
    var pointsOfInterest: List<String>,
    val imgs: List<String>,
    var votesForApproval: Long,
    var isApproved: Boolean,
    var votesForRemoval: Long,
    var isBeingRemoved: Boolean,
)

/**
 * Represents a category for points of interest
 */
data class Category(
    var id: String,
    val userId: String,
    var name: String,
    var description: String,
    val img: String,
    var votesForApproval: Long,
    var isApproved: Boolean,
    var votesForRemoval: Long,
    var isBeingRemoved: Boolean,
)

/**
 * Represents a point of interest from N locations, that has a category
 */
data class PointOfInterest(
    var id: String,
    val userId: String,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val isManualCoords: Boolean,
    val locations: MutableList<String>,
    var category: String,
    val imgs: List<String>,
    var classification: Long,
    var nClassifications: Long,
    var votesForApproval: Long,
    var isApproved: Boolean,
    var votesForRemoval: Long,
    var isBeingRemoved: Boolean,
)

class GeoData(private val firebaseGeoDataService: FirebaseGeoDataService) {

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
        firebaseGeoDataService.startObserverGeoData(
            onNewLocations = { locsMapList ->
                if (locsMapList.isNotEmpty()) {
                    val newLocations: MutableList<Location> = mutableListOf()
                    locsMapList.map { i ->
                        firebaseGeoDataService.downloadImages(
                            "locations/" + i["id"],
                            i["imgs"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newLocations.add(
                                    Location(
                                        id = i["id"] as String,
                                        userId = i["userID"] as String,
                                        name = i["name"] as String,
                                        description = i["description"] as String,
                                        lat = i["lat"] as Double,
                                        long = i["long"] as Double,
                                        isManualCoords = i["isManualCoords"] as Boolean,
                                        pointsOfInterest = i["pointsOfInterest"] as List<String>,
                                        imgs = paths,
                                        votesForApproval = i["votesForApproval"] as Long,
                                        isApproved = i["isApproved"] as Boolean,
                                        votesForRemoval = i["votesForRemoval"] as Long,
                                        isBeingRemoved = i["isBeingRemoved"] as Boolean
                                    )
                                )
                            }
                            if (locsMapList.size == newLocations.size)
                                _locations.value = newLocations.toList()
                        }
                    }
                }
            },
            onNewPointsOfInterest = { pointsMapList ->
                if (pointsMapList.isNotEmpty()) {
                    val newPointsOfInterest: MutableList<PointOfInterest> = mutableListOf()
                    pointsMapList.map { i ->
                        firebaseGeoDataService.downloadImages(
                            "pointsofinterest/" + i["id"],
                            i["imgs"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newPointsOfInterest.add(
                                    PointOfInterest(
                                        id = i["id"] as String,
                                        userId = i["userID"] as String,
                                        name = i["name"] as String,
                                        description = i["description"] as String,
                                        lat = i["lat"] as Double,
                                        long = i["long"] as Double,
                                        isManualCoords = i["isManualCoords"] as Boolean,
                                        locations = i["locations"] as MutableList<String>,
                                        category = i["category"] as String,
                                        imgs = paths,
                                        classification = i["classification"] as Long,
                                        nClassifications = i["nClassifications"] as Long,
                                        votesForApproval = i["votesForApproval"] as Long,
                                        isApproved = i["isApproved"] as Boolean,
                                        votesForRemoval = i["votesForRemoval"] as Long,
                                        isBeingRemoved = i["isBeingRemoved"] as Boolean
                                    )
                                )
                            }
                            if (pointsMapList.size == newPointsOfInterest.size)
                                _pointsOfInterest.value = newPointsOfInterest.toList()
                        }
                    }
                }
            },
            onNewCategories = { catsMapList ->
                if (catsMapList.isNotEmpty()) {
                    val newCategories: MutableList<Category> = mutableListOf()
                    catsMapList.map { i ->
                        firebaseGeoDataService.downloadImages(
                            "categories/" + i["id"],
                            i["img"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newCategories.add(
                                    Category(
                                        id = i["id"] as String,
                                        userId = i["userID"] as String,
                                        name = i["name"] as String,
                                        description = i["description"] as String,
                                        img = paths[0],
                                        votesForApproval = i["votesForApproval"] as Long,
                                        isApproved = i["isApproved"] as Boolean,
                                        votesForRemoval = i["votesForRemoval"] as Long,
                                        isBeingRemoved = i["isBeingRemoved"] as Boolean
                                    )
                                )
                            }
                            if (catsMapList.size == newCategories.size)
                                _categories.value = newCategories.toList()
                        }
                    }
                }
            }
        )
    }

    /* ------------------------  Update and delete info to Firestore (Start) ------------------------ */

    fun updateLocation(
        locationId: String,
    ) {
        firebaseGeoDataService.updateLocationToFirestore(
            locationId,
            _locations.value.find { it.id == locationId }) {
        }
    }

    fun deleteLocation(
        locationId: String,
    ) {
        firebaseGeoDataService.deleteLocationFromFirestore(
            locationId,
            _locations.value.find { it.id==locationId }!!.imgs
        ) {
        }
    }

    fun updatePointOfInterest(
        pointOfInterestId: String,
    ) {
        firebaseGeoDataService.updatePointOfInterestToFirestore(
            pointOfInterestId,
            _pointsOfInterest.value.find { it.id == pointOfInterestId }) {
        }
    }

    fun updateCategory(
        categoryId: String,
    ) {
        firebaseGeoDataService.updateCategoryToFirestore(
            categoryId,
            _categories.value.find { it.id == categoryId }) {
        }
    }

    fun deleteCategory(
        categoryId: String,
    ) {
        firebaseGeoDataService.deleteCategoryFromFirestore(
            categoryId
        ) {
        }
    }

    /* ------------------------  Update and delete info to Firestore (End) ------------------------ */

    /* ------------------------  Add and edit info (Start) ------------------------ */

    fun addLocation(
        userId: String,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        imgs: List<String>,
        onResult: (Throwable?) -> Unit
    ) {

        firebaseGeoDataService.addLocationToFirestore(
            Location(
                "",
                userId,
                name,
                description,
                lat,
                long,
                isManualCoords,
                mutableListOf(),
                imgs,
                0L,
                false,
                0L,
                false
            )
        ) { exception ->
            onResult(exception)
        }

    }

    fun addPointOfInterest(
        userId: String,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        locations: MutableList<String>,
        category: String,
        imgs: List<String>,
        onResult: (Throwable?) -> Unit
    ) {

        firebaseGeoDataService.addPointOfInterestToFirestore(
            PointOfInterest(
                "",
                userId,
                name,
                description,
                lat,
                long,
                isManualCoords,
                locations,
                category,
                imgs,
                0,
                0L,
                0L,
                false,
                0L,
                false
            )
        ) { exception ->
            onResult(exception)
        }

        for (i in _locations.value)
            if (locations.any { i.name == it }) {
                val newLocation = i
                newLocation.pointsOfInterest = newLocation.pointsOfInterest + name
                firebaseGeoDataService.updateLocationToFirestore(i.id, newLocation) {}
            }

    }

    fun addCategory(
        userId: String,
        name: String,
        description: String,
        img: String,
        onResult: (Throwable?) -> Unit
    ) {

        firebaseGeoDataService.addCategoryToFirestore(
            Category(
                "",
                userId,
                name,
                description,
                img,
                0L,
                false,
                0L,
                false
            )
        ) { exception ->
            onResult(exception)
        }

    }

    fun editLocation(
        locationId: String,
        newName: String,
        newDescription: String,
        newLat: Double,
        newLong: Double,
        newIsManual: Boolean
    ) {
        val tempLocation = _locations.value.find { it.id == locationId }
        tempLocation?.name = newName
        tempLocation?.description = newDescription
        tempLocation?.lat = newLat
        tempLocation?.long = newLong
        tempLocation?.isManualCoords = newIsManual
        tempLocation?.votesForApproval = 0
        tempLocation?.isApproved = false
    }

    fun editCategory(
        categoryId: String,
        newName: String,
        newDescription: String
    ) {
        val tempCategory = _categories.value.find { it.id == categoryId }
        tempCategory?.name = newName
        tempCategory?.description = newDescription
        tempCategory?.votesForApproval = 0
        tempCategory?.isApproved = false
    }

    fun changeCategoryOfPoint(
        pointOfInterestId: String,
        newCategoryName: String
    ) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.category = newCategoryName
    }

    fun changeLocationOfPoint(
        pointOfInterestId: String,
        currentLocationName: String,
        newLocationName: String
    ) {
        val pointToChange = _pointsOfInterest.value.find { it.id == pointOfInterestId }
        val indexToChange = pointToChange?.locations?.indexOfFirst { it == currentLocationName }
        if (indexToChange != null)
            pointToChange.locations.set(indexToChange, newLocationName)
    }

    /* ------------------------  Add and edit info (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun voteForApprovalLocation(locationId: String) {
        _locations.value.find { it.id == locationId }?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalLocation(locationId: String) {
        val tempLoc = _locations.value.find { it.id == locationId }
        if (tempLoc?.votesForApproval?.toInt() != 0)
            tempLoc?.apply { votesForApproval-- }
    }

    fun approveLocation(locationId: String) {
        _locations.value.find { it.id == locationId }?.apply { isApproved = true }
    }

    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun voteForApprovalPointOfInterest(pointOfInterestId: String) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }
            ?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalPointOfInterest(pointOfInterestId: String) {
        val tempPointOfInterest = _pointsOfInterest.value.find { it.id == pointOfInterestId }
        if (tempPointOfInterest?.votesForApproval?.toInt() != 0)
            tempPointOfInterest?.apply { votesForApproval-- }
    }

    fun approvePointOfInterest(pointOfInterestId: String) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.apply { isApproved = true }
    }

    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Category approval (Start) ------------------------ */
    fun voteForApprovalCategory(categoryId: String) {
        _categories.value.find { it.id == categoryId }?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalCategory(categoryId: String) {
        _categories.value.find { it.id == categoryId }?.apply { votesForApproval-- }
    }

    fun approveCategory(categoryId: String) {
        val tempCategory = _categories.value.find { it.id == categoryId }
        if (tempCategory?.votesForApproval?.toInt() != 0)
            tempCategory?.apply { isApproved = true }
    }

    /* ------------------------  Category approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addClassificationToPoint(pointOfInterestId: String, classification: Long) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.classification =
            _pointsOfInterest.value.find { it.id == pointOfInterestId }?.classification!! + classification
    }

    fun incrementNumberOfClassifications(pointOfInterestId: String) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.nClassifications =
            _pointsOfInterest.value.find { it.id == pointOfInterestId }?.nClassifications!! + 1
    }

    fun removeClassificationToPoint(pointOfInterestId: String, classification: Long) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.classification =
            _pointsOfInterest.value.find { it.id == pointOfInterestId }?.classification!! - classification
    }

    fun decrementNumberOfClassifications(pointOfInterestId: String) {
        _pointsOfInterest.value.find { it.id == pointOfInterestId }?.nClassifications =
            _pointsOfInterest.value.find { it.id == pointOfInterestId }?.nClassifications!! - 1
    }
    /* ------------------------  Point classification (End) ------------------------ */
}