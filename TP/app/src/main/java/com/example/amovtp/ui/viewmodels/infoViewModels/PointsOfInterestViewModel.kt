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
     * Holds the filter used to get locations by the name
     */
    private var _locationNameFilter = mutableStateOf("")

    /**
     * Holds the filter used to get categories by the name
     */
    private var _categoryNameFilter = mutableStateOf("")

    /**
     * Holds the string used to ask for every location
     */
    private var _allLocationsString = mutableStateOf("")

    /**
     * Holds the string used to ask for every category
     */
    private var _allCategoriesString = mutableStateOf("")

    /**
     * Set for the string used to ask for every location
     */
    fun setAllLocationsString(value: String) {
        _allLocationsString.value = value
    }

    /**
     * Set for the filter used to get locations by the name
     */
    fun setLocationNameFilter(value: String) {
        _locationNameFilter.value = value
    }

    /**
     * Set for the string used to ask for every category and the filter used to get categories by the name
     */
    fun setAllCategoriesStringAndCategoryName(value: String) {
        _allCategoriesString.value = value
        _categoryNameFilter.value = value
    }

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
    private fun getPointsFromCategory(categoryName: String, listOfCurrentPoints:List<PointOfInterest>): List<PointOfInterest> {
        return listOfCurrentPoints.filter { it.category == categoryName }
    }

    /**
     * Gets every point of interest with a specific location and a specific category
     */
    fun getPointsWithFilters(locationName: String, categoryName: String): List<PointOfInterest> {

        if (locationName.isNotEmpty())
            _locationNameFilter.value = locationName
        else
            _locationNameFilter.value = _allLocationsString.value

        if (!categoryName.isNotEmpty())
            _categoryNameFilter.value = categoryName
        else
            _categoryNameFilter.value = _allCategoriesString.value

        val filteredPoints: List<PointOfInterest> = if (_locationNameFilter.value == _allLocationsString.value)
            geoData.getPointsOfInterest()
        else
            getPointsFromLocation(_locationNameFilter.value)


        return if (_categoryNameFilter.value == _allCategoriesString.value)
            filteredPoints
        else
            getPointsFromCategory(_categoryNameFilter.value,filteredPoints)

    }

    fun getPointsOfInterestOrderedByDistance(pointsOfInterest: List<PointOfInterest>): List<PointOfInterest>{

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

    fun calculateDistance(
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