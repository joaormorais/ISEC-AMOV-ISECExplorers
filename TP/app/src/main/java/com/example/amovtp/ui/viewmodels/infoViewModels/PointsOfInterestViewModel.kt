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
import com.example.amovtp.utils.codes.Codes

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


    /**
     * Gets the current location of the android device
     */
    fun getCurrentLocation(): MutableLiveData<android.location.Location> {
        return usersData.getCurrentLocation()
    }

    /**
     * Gets every location
     */
    fun getLocations(): List<Location> {
        return geoData.getLocations()
    }

    /**
     * Gets every category
     */
    fun getCategories(): List<Category> {
        return geoData.getCategories()
    }

    /**
     * Gets every point of interest
     */
    fun getPointsOfInterest(): List<PointOfInterest> {
        return geoData.getPointsOfInterest()
    }

    /**
     * Gets every point of interest with a specific location
     */
    fun getPointsFromLocation(locationName: String): List<PointOfInterest> {
        return geoData.getPointsOfInterest().filter { it.locations.contains(locationName) }
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




    private var _filterLocationName = mutableStateOf(Codes.ALL_LOCATIONS.toString())
    private var _filterCategoryName = mutableStateOf(Codes.ALL_CATEGORIES.toString())

    /**
     * Gets every point of interest with a specific location and a specific category
     */
    fun tempFiltering(locationName: String?, categoryName: String?): List<PointOfInterest> {

        if (locationName == null)
            _filterCategoryName.value = categoryName!!
        else if (categoryName == null)
            _filterLocationName.value = locationName

        var filteredPoints: List<PointOfInterest>

        if (_filterLocationName.value == Codes.ALL_LOCATIONS.toString())
            filteredPoints = geoData.getPointsOfInterest()
        else
            filteredPoints = getPointsFromLocation(_filterLocationName.value)

        if (_filterCategoryName.value == Codes.ALL_CATEGORIES.toString())
            return filteredPoints
        else
            return getPointsFromCategory(_filterCategoryName.value, filteredPoints)

    }

    /**
     * Gets points ordered by distance
     */
    fun getPointsOfInterestOrderedByDistance(pointsOfInterest: List<PointOfInterest>): List<PointOfInterest> {

        val currentLocation = usersData.getCurrentLocation()

        currentLocation.value?.latitude = usersData.getCurrentLocation().value!!.latitude
        currentLocation.value?.longitude = usersData.getCurrentLocation().value!!.longitude

        return pointsOfInterest.sortedBy { pointOfInterest ->

            calculateDistance(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude,
                pointOfInterest.lat,
                pointOfInterest.long
            )

        }
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