package com.example.amovtp.data

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData

class UsersData(/*firestore*/) {

    private val _currentLocation = MutableLiveData(Location(null))
    private var _locationsApproved = mutableListOf<Int>()
    private var _pointsOfInterestApproved = mutableListOf<Int>()
    private var _categoriesApproved = mutableListOf<Int>()
    private var _pointsOfInterestClassified = mutableMapOf<Int, Int>()

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val locationsApproved: List<Int>
        get() = _locationsApproved.toList()

    val pointsOfInterestApproved: List<Int>
        get() = _pointsOfInterestApproved.toList()

    val categoriesApproved: List<Int>
        get() = _categoriesApproved.toList()

    val pointsOfInterestClassified: Map<Int, Int>
        get() = _pointsOfInterestClassified.toMap()

    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }

    fun addLocationApproved(locationId: Int) {
        _locationsApproved.add(locationId)
    }

    fun removeLocationApproved(locationId: Int) {
        _locationsApproved.remove(locationId)
    }

    fun addPointOfInterestApproved(pointOfInterestId: Int) {
        _pointsOfInterestApproved.add(pointOfInterestId)
    }

    fun removePointOfInterestApproved(pointOfInterestId: Int) {
        _pointsOfInterestApproved.remove(pointOfInterestId)
    }

    fun addCategoryApproved(categoryId: Int) {
        _categoriesApproved.add(categoryId)
    }

    fun removeCategoryApproved(categoryId: Int) {
        _categoriesApproved.add(categoryId)
    }

    fun addPointOfInterestClassified(pointOfInterestId: Int, classification: Int) {
        _pointsOfInterestClassified[pointOfInterestId] = classification
    }

    fun removePointOfInterestClassified(pointOfInterestId: Int) {
        _pointsOfInterestClassified.remove(pointOfInterestId)
    }

}