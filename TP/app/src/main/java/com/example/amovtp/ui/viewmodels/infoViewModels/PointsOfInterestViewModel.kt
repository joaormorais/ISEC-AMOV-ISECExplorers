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

    fun findVoteForApprovedPointOfInterestByUser(pointOfInterestName: String): Boolean {
        return userData.localUser.value.pointsOfInterestApproved.any { it == pointOfInterestName }
    }

    fun voteForApprovalPointOfInterestByUser(pointOfInterestName: String) {
        geoData.voteForApprovalPointOfInterest(pointOfInterestName)
        userData.addPointOfInterestApproved(pointOfInterestName)
        if (geoData.pointsOfInterest.value.find { it.name == pointOfInterestName }?.votesForApproval!! >= Consts.VOTES_NEEDED_FOR_APPROVAL)
            geoData.approvePointOfInterest(pointOfInterestName)
        geoData.editPointOfInterest(pointOfInterestName)
    }

    fun removeVoteForApprovalPointOfInterestByUser(pointOfInterestName: String) {
        geoData.removeVoteForApprovalPointOfInterest(pointOfInterestName)
        userData.removePointOfInterestApproved(pointOfInterestName)
        geoData.editPointOfInterest(pointOfInterestName)
    }

    fun findClassificationFromUser(pointOfInterestName: String): Double {

        return if (userData.localUser.value.pointsOfInterestClassified.keys.contains(pointOfInterestName))
            userData.localUser.value.pointsOfInterestClassified.getValue(pointOfInterestName)
        else
            Consts.NO_START_CLASSIFICATION

    }

    fun addClassificationToPointByUser(pointOfInterestName: String, classification: Double) {
        if (userData.localUser.value.pointsOfInterestClassified.containsKey(pointOfInterestName)) {
            removeClassificationToPointByUser(pointOfInterestName)
        }
        geoData.addClassificationToPoint(pointOfInterestName, classification)
        geoData.incrementNumberOfClassifications(pointOfInterestName)
        userData.addPointOfInterestClassified(pointOfInterestName, classification)
        geoData.editPointOfInterest(pointOfInterestName)
    }

    fun removeClassificationToPointByUser(pointOfInterestName: String) {
        geoData.removeClassificationToPoint(
            pointOfInterestName,
            findClassificationFromUser(pointOfInterestName)
        )
        geoData.decrementNumberOfClassifications(pointOfInterestName)
        userData.removePointOfInterestClassified(pointOfInterestName)
        geoData.editPointOfInterest(pointOfInterestName)
    }

    fun calculateMediaClassification(pointOfInterestName: String): Double {
        val tempPoint = geoData.pointsOfInterest.value.find { it.name == pointOfInterestName }
        return tempPoint!!.classification.div(tempPoint.nClassifications)
    }

}