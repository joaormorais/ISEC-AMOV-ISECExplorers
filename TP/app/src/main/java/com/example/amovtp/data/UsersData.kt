package com.example.amovtp.data

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData

class UsersData(/*firestore*/) {

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")
    private val _currentLocation = MutableLiveData(Location(null))
    private var _locationsApproved = mutableListOf<Int>()
    private var _pointsOfInterestApproved = mutableListOf<Int>()
    private var _categoriesApproved = mutableListOf<Int>()

    val username: String
        get() = _username.toString()

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val locationsApproved: List<Int>
        get() = _locationsApproved.toList()

    val pointsOfInterestApproved: List<Int>
        get() = _pointsOfInterestApproved.toList()

    val categoriesApproved: List<Int>
        get() = _categoriesApproved.toList()

    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }

    fun setUsername(newValue: String) {
        _username.value = newValue
    }

    fun setPassword(newValue: String) {
        _password.value = newValue
    }

    fun addLocationApproved(locationId: Int) {
        _locationsApproved.add(locationId)
    }

    fun addPointOfInterestApproved(pointOfInterestId: Int) {
        _pointsOfInterestApproved.add(pointOfInterestId)
    }

    fun addCategoryApproved(categoryId: Int) {
        _categoriesApproved.add(categoryId)
    }
}