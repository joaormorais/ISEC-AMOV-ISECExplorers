package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class PointsOfInterestViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PointsOfInterestViewModel(geoData, userData) as T
    }
}

class PointsOfInterestViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    private var _filterLocationName = mutableStateOf(Consts.ALL_LOCATIONS)
    private var _filterCategoryName = mutableStateOf(Consts.ALL_CATEGORIES)

    /**
     * Calculates the distance between a point and the current location of the device
     */
    private fun calculateDistance(
        currLat: Double, // lat1
        currLong: Double, // long1
        locLat: Double, // lat2
        locLong: Double // long2
    ): Double {

        val earthRadius = 6371
        var latDistance = Math.toRadians(locLat - currLat)
        var longDistance = Math.toRadians(locLong - currLong)

        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(currLat)) * Math.cos(Math.toRadians(locLat)) *
                Math.sin(longDistance / 2) * Math.sin(longDistance / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c

    }

    /**
     * Gets the current location of the android device
     */
    fun getCurrentLocation(): MutableLiveData<android.location.Location> {
        return userData.currentLocation
    }

    /**
     * Gets every location
     */
    fun getLocations(): List<Location> {
        return geoData.locations
    }

    /**
     * Gets every category
     */
    fun getCategories(): List<Category> {
        return geoData.categories
    }

    /**
     * Gets every point of interest
     */
    fun getPointsOfInterest(): List<PointOfInterest> {
        return geoData.pointsOfInterest
    }

    /**
     * Gets every point of interest with a specific location
     */
    fun getPointsFromLocation(locationName: String): List<PointOfInterest> {
        return geoData.pointsOfInterest.filter { it.locations.contains(locationName) }
    }

    /**
     * Gets every point of interest with a specific category
     */
    private fun getPointsFromCategory(
        categoryName: String,
        listOfCurrentPoints: List<PointOfInterest>
    ): List<PointOfInterest> {
        return listOfCurrentPoints.filter { it.category == categoryName }
    }

    /**
     * Gets every point of interest with a specific location and a specific category
     */
    fun getPointsWithFilters(locationName: String?, categoryName: String?): List<PointOfInterest> {

        if (locationName == null)
            _filterCategoryName.value = categoryName!!
        else if (categoryName == null)
            _filterLocationName.value = locationName

        var filteredPoints: List<PointOfInterest>

        if (_filterLocationName.value == Consts.ALL_LOCATIONS)
            filteredPoints = geoData.pointsOfInterest
        else
            filteredPoints = getPointsFromLocation(_filterLocationName.value)

        if (_filterCategoryName.value == Consts.ALL_CATEGORIES)
            return filteredPoints
        else
            return getPointsFromCategory(_filterCategoryName.value, filteredPoints)

    }

    /**
     * Calculates the distance between every point of interest, and the current location
     */
    fun getPointsOfInterestOrderedByDistance(pointsOfInterest: List<PointOfInterest>): List<PointOfInterest> {

        val currentLocation = userData.currentLocation

        return pointsOfInterest.sortedBy { pointOfInterest ->

            calculateDistance(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude,
                pointOfInterest.lat,
                pointOfInterest.long
            )

        }
    }

    fun findVoteForApprovedPointOfInterestByUser(pointOfInterestId: Long): Boolean {
        return userData.pointsOfInterestApproved.any { it == pointOfInterestId }
    }

    fun voteForApprovalPointOfInterestByUser(pointOfInterestId: Long) {
        geoData.voteForApprovalPointOfInterest(pointOfInterestId)
        userData.addPointOfInterestApproved(pointOfInterestId)
        if (geoData.pointsOfInterest.find { it.id == pointOfInterestId }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL)
            geoData.approvePointOfInterest(pointOfInterestId)
    }

    fun removeVoteForApprovalPointOfInterestByUser(pointOfInterestId: Long) {
        geoData.removeVoteForApprovalPointOfInterest(pointOfInterestId)
        userData.removePointOfInterestApproved(pointOfInterestId)
    }

    fun findClassificationFromUser(pointOfInterestID: Long):Double{

        return if(userData.pointsOfInterestClassified.keys.contains(pointOfInterestID))
            userData.pointsOfInterestClassified.getValue(pointOfInterestID)
        else
            Consts.NO_START_CLASSIFICATION

    }

    fun addClassificationToPointByUser(pointOfInterestID: Long, classification: Double) {
        if (userData.pointsOfInterestClassified.containsKey(pointOfInterestID)) {
            removeClassificationToPointByUser(pointOfInterestID)
        }

        geoData.addClassificationToPoint(pointOfInterestID, classification)
        geoData.incrementNumberOfClassifications(pointOfInterestID)
        userData.addPointOfInterestClassified(pointOfInterestID,classification)
    }

    fun removeClassificationToPointByUser(pointOfInterestID: Long) {
        geoData.removeClassificationToPoint(
            pointOfInterestID,
            findClassificationFromUser(pointOfInterestID)
        )
        geoData.decrementNumberOfClassifications(pointOfInterestID)
        userData.removePointOfInterestClassified(pointOfInterestID)
    }

    fun calculateMediaClassification(pointOfInterestID: Long): Double {
        val tempPoint = geoData.pointsOfInterest.find { it.id == pointOfInterestID }
        return tempPoint!!.classification.div(tempPoint.nClassifications)
    }

}