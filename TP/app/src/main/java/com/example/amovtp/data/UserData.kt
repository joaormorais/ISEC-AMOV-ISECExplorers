package com.example.amovtp.data

import android.location.Location
import androidx.lifecycle.MutableLiveData

class UserData(/*firestore*/) {

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

    /* ------------------------  Device location (Start) ------------------------ */
    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }
    /* ------------------------  Device location (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun addLocationApproved(locationId: Int) {
        _locationsApproved.add(locationId)
    }

    fun removeLocationApproved(locationId: Int) {
        _locationsApproved.remove(locationId)
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addPointOfInterestApproved(pointOfInterestId: Int) {
        _pointsOfInterestApproved.add(pointOfInterestId)
    }

    fun removePointOfInterestApproved(pointOfInterestId: Int) {
        _pointsOfInterestApproved.remove(pointOfInterestId)
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addCategoryApproved(categoryId: Int) {
        _categoriesApproved.add(categoryId)
    }

    fun removeCategoryApproved(categoryId: Int) {
        _categoriesApproved.add(categoryId)
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addPointOfInterestClassified(pointOfInterestId: Int, classification: Int) {
        _pointsOfInterestClassified[pointOfInterestId] = classification
    }

    fun removePointOfInterestClassified(pointOfInterestId: Int) {
        _pointsOfInterestClassified.remove(pointOfInterestId)
    }
    /* ------------------------  Point classification (End) ------------------------ */
}