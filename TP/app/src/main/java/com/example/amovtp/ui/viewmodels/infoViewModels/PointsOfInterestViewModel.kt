package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.data.UsersData
import com.example.amovtp.utils.Consts

class PointsOfInterestViewModelFactory(
    private val geoData: GeoData,
    private val usersData: UsersData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PointsOfInterestViewModel(geoData, usersData) as T
    }
}

class PointsOfInterestViewModel(
    private val geoData: GeoData,
    private val usersData: UsersData
) : ViewModel() {

    private var _filterLocationName = mutableStateOf(Consts.ALL_LOCATIONS)
    private var _filterCategoryName = mutableStateOf(Consts.ALL_CATEGORIES.toString())

    /**
     * Gets the current location of the android device
     */
    fun getCurrentLocation(): MutableLiveData<android.location.Location> {
        return usersData.currentLocation
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

        if (_filterLocationName.value == Consts.ALL_LOCATIONS.toString())
            filteredPoints = geoData.pointsOfInterest
        else
            filteredPoints = getPointsFromLocation(_filterLocationName.value)

        if (_filterCategoryName.value == Consts.ALL_CATEGORIES.toString())
            return filteredPoints
        else
            return getPointsFromCategory(_filterCategoryName.value, filteredPoints)

    }

    /**
     * Calculates the distance between every point of interest, and the current location
     */
    fun getPointsOfInterestOrderedByDistance(pointsOfInterest: List<PointOfInterest>): List<PointOfInterest> {

        val currentLocation = usersData.currentLocation

        return pointsOfInterest.sortedBy { pointOfInterest ->

            calculateDistance(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude,
                pointOfInterest.lat,
                pointOfInterest.long
            )

        }
    }

    fun findVoteForApprovedPointOfInterest(pointOfInterestId: Int): Boolean {
        return usersData.pointsOfInterestApproved.any { it == pointOfInterestId }
    }

    fun voteForApprovalPointOfInterest(pointOfInterestId: Int) {
        geoData.voteForApprovalPointOfInterest(pointOfInterestId)
        usersData.addPointOfInterestApproved(pointOfInterestId)
        if (geoData.pointsOfInterest.find { it.id == pointOfInterestId }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL)
            geoData.approvePointOfInterest(pointOfInterestId)
    }

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

}