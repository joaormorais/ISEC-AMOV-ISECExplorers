package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.LocalUser
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
        val latDistance = Math.toRadians(locLat - currLat)
        val longDistance = Math.toRadians(locLong - currLong)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(currLat)) * cos(Math.toRadians(locLat)) *
                sin(longDistance / 2) * sin(longDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c

    }

    /**
     * Gets every point of interest from a specific category
     */
    private fun getPointsFromCategory(
        categoryName: String,
        listOfCurrentPoints: List<PointOfInterest>
    ): List<PointOfInterest> {
        return listOfCurrentPoints.filter { it.category == categoryName }
    }

    fun getLocalUser(): MutableState<LocalUser> {
        return userData.localUser
    }

    fun getCurrentLocation(): MutableLiveData<android.location.Location> {
        return userData.currentLocation
    }

    fun getLocations(): MutableState<List<Location>> {
        return geoData.locations
    }

    fun getCategories(): MutableState<List<Category>> {
        return geoData.categories
    }

    fun getPointsOfInterest(): MutableState<List<PointOfInterest>> {
        return geoData.pointsOfInterest
    }

    fun getPointsFromLocation(locationName: String): List<PointOfInterest> {
        return geoData.pointsOfInterest.value.filter { it.locations.contains(locationName) }
    }

    fun getPointsWithFilters(locationName: String?, categoryName: String?): List<PointOfInterest> {

        if (locationName == null)
            _filterCategoryName.value = categoryName!!
        else if (categoryName == null)
            _filterLocationName.value = locationName

        val filteredPoints: List<PointOfInterest> =
            if (_filterLocationName.value == Consts.ALL_LOCATIONS)
                geoData.pointsOfInterest.value
            else
                getPointsFromLocation(_filterLocationName.value)

        if (_filterCategoryName.value == Consts.ALL_CATEGORIES)
            return filteredPoints
        else
            return getPointsFromCategory(_filterCategoryName.value, filteredPoints)

    }

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

    fun findVoteForApprovedPointOfInterestByUser(pointOfInterestId: String): Boolean {
        return userData.localUser.value.pointsOfInterestApproved.any { it == pointOfInterestId }
    }

    fun voteForApprovalPointOfInterestByUser(pointOfInterestId: String) {
        geoData.voteForApprovalPointOfInterest(pointOfInterestId)
        userData.addPointOfInterestApproved(pointOfInterestId)
        if (geoData.pointsOfInterest.value.find { it.id == pointOfInterestId }?.votesForApproval!! >= Consts.VOTES_NEEDED_FOR_APPROVAL)
            geoData.approvePointOfInterest(pointOfInterestId)
        geoData.updatePointOfInterest(pointOfInterestId)
        userData.updateLocalUser()
    }

    fun removeVoteForApprovalPointOfInterestByUser(pointOfInterestId: String) {
        geoData.removeVoteForApprovalPointOfInterest(pointOfInterestId)
        userData.removePointOfInterestApproved(pointOfInterestId)
        geoData.updatePointOfInterest(pointOfInterestId)
        userData.updateLocalUser()
    }

    fun findClassificationFromUser(pointOfInterestId: String): Long {

        return if (userData.localUser.value.pointsOfInterestClassified.keys.contains(pointOfInterestId))
            userData.localUser.value.pointsOfInterestClassified.getValue(pointOfInterestId)
        else
            Consts.NO_START_CLASSIFICATION

    }

    fun addClassificationToPointByUser(pointOfInterestId: String, classification: Long) {
        if (userData.localUser.value.pointsOfInterestClassified.containsKey(pointOfInterestId)) {
            removeClassificationToPointByUser(pointOfInterestId)
        }
        geoData.addClassificationToPoint(pointOfInterestId, classification)
        geoData.incrementNumberOfClassifications(pointOfInterestId)
        userData.addPointOfInterestClassified(pointOfInterestId, classification)
        geoData.updatePointOfInterest(pointOfInterestId)
        userData.updateLocalUser()
    }

    fun removeClassificationToPointByUser(pointOfInterestId: String) {
        geoData.removeClassificationToPoint(
            pointOfInterestId,
            findClassificationFromUser(pointOfInterestId)
        )
        geoData.decrementNumberOfClassifications(pointOfInterestId)
        userData.removePointOfInterestClassified(pointOfInterestId)
        geoData.updatePointOfInterest(pointOfInterestId)
        userData.updateLocalUser()
    }

    fun calculateMediaClassification(pointOfInterestId: String): Double {
        val tempPoint = geoData.pointsOfInterest.value.find { it.id == pointOfInterestId }
        return tempPoint!!.classification.toDouble().div(tempPoint.nClassifications)
    }

}