package com.example.amovtp.data

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.example.amovtp.services.FirebaseGeoDataService

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
    val userId: String,
    val name: String,
    val description: String,
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
                            "locations/" + i["name"],
                            i["imgs"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newLocations.add(
                                    Location(
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
                            "pointsofinterest/" + i["name"],
                            i["imgs"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newPointsOfInterest.add(
                                    PointOfInterest(
                                        userId = i["userID"] as String,
                                        name = i["name"] as String,
                                        description = i["description"] as String,
                                        lat = i["lat"] as Double,
                                        long = i["long"] as Double,
                                        isManualCoords = i["isManualCoords"] as Boolean,
                                        locations = i["locations"] as List<String>,
                                        category = i["category"] as String,
                                        imgs = paths,
                                        classification = i["classification"] as Double,
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
                            "categories/" + i["name"],
                            i["img"] as List<String>
                        ) { paths ->
                            if (paths.isNotEmpty()) {
                                newCategories.add(
                                    Category(
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

    /* ------------------------  Add, remove, and edit info (Start) ------------------------ */

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
        ) {
        //TODO: sandra receber o popup e enviar para a VM (se a exception for null é porque deu sucesso)
            exception ->
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
        locations: List<String>,
        category: String,
        imgs: List<String>,
        onResult: (Throwable?) -> Unit
    ) {

        firebaseGeoDataService.addPointOfInterestToFirestore(
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
                false,
                0L,
                false
            )
        ) {
            //TODO: sandra receber o popup e enviar para a VM (se a exception for null é porque deu sucesso)
            exception ->
            onResult(exception)
        }

        for (i in _locations.value)
            if (locations.any { i.name == it }) {
                val newLocation = i
                newLocation.pointsOfInterest = newLocation.pointsOfInterest + name
                firebaseGeoDataService.updateLocationToFirestore(i.name, newLocation) {}
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
                userId,
                name,
                description,
                img,
                0L,
                false,
                0L,
                false
            )
        ) {
            //TODO: sandra receber o popup e enviar para a VM (se a exception for null é porque deu sucesso)
            exception ->
            onResult(exception)
        }

    }

    fun editLocation(
        locationName:String
    ){
        firebaseGeoDataService.updateLocationToFirestore(locationName,_locations.value.find { it.name == locationName }){
            //TODO: tratar a exception
        }
    }

    fun editPointOfInterest(
        pointOfInterestName:String
    ){
        firebaseGeoDataService.updatePointOfInterestToFirestore(pointOfInterestName,_pointsOfInterest.value.find { it.name == pointOfInterestName }){
            //TODO: tratar a exception
        }
    }

    fun editCategory(
        categoryName:String
    ){
        firebaseGeoDataService.updateCategoryToFirestore(categoryName,_categories.value.find { it.name == categoryName }){
            //TODO: tratar a exception
        }
    }

    /* ------------------------  Add, remove, and edit info (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun voteForApprovalLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalLocation(locationName: String) {
        _locations.value.find { it.name == locationName }?.apply { votesForApproval-- }

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
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalPointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { votesForApproval-- }
    }

    fun approvePointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { isApproved = true }
    }

    fun disapprovePointOfInterest(pointOfInterestName: String) {
        _pointsOfInterest.value.find { it.name == pointOfInterestName }?.apply { isApproved = false }
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Category approval (Start) ------------------------ */
    fun voteForApprovalCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { votesForApproval++ }
    }

    fun removeVoteForApprovalCategory(categoryName: String) {
        _categories.value.find { it.name == categoryName }?.apply { votesForApproval-- }
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