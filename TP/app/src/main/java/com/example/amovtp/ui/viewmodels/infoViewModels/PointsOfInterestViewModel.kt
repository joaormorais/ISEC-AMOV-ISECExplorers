package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest

class PointsOfInterestViewModelFactory(private val geoData: GeoData) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PointsOfInterestViewModel(geoData) as T
    }
}

class PointsOfInterestViewModel(private val geoData: GeoData) : ViewModel() {

    private var _locationNameFilter = mutableStateOf("")
    private var _categoryNameFilter = mutableStateOf("")
    private var _allLocationsString = mutableStateOf("")
    private var _allCategoriesString = mutableStateOf("")

    fun setAllLocationsString(value: String) {
        _allLocationsString.value = value
    }

    fun setLocationNameFilter(value: String) {
        _locationNameFilter.value = value
    }

    fun setAllCategoriesStringAndFilter(value: String) {
        _allCategoriesString.value = value
        _categoryNameFilter.value = value
    }

    fun getPointsOfInterest(): List<PointOfInterest> {

        return geoData.getPointsOfInterest()

    }

    fun getLocations(): List<Location> {
        return geoData.getLocations()
    }

    fun getCategories(): List<Category> {
        return geoData.getCategories()
    }

    fun getPointsFromLocation(locationName: String?): List<PointOfInterest> {
        return geoData.getPointsOfInterest().filter { it.locations.contains(locationName) }
    }

    fun getPointsWithFilters(locationName: String, categoryName: String): List<PointOfInterest> {

        var filteredPoints: List<PointOfInterest>

        if (!locationName.isEmpty())
            _locationNameFilter.value = locationName

        if (!categoryName.isEmpty())
            _categoryNameFilter.value = categoryName

        if (_locationNameFilter.value == _allLocationsString.value)
            filteredPoints = geoData.getPointsOfInterest()
        else
            filteredPoints =
                geoData.getPointsOfInterest()
                    .filter { it.locations.contains(_locationNameFilter.value) }

        if (_categoryNameFilter.value == _allCategoriesString.value)
            return filteredPoints
        else
            return filteredPoints.filter { it.category == _categoryNameFilter.value }

    }


}