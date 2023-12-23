package com.example.amovtp.data

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.amovtp.utils.fb.FAuthUtil

class UserData(private val fAuthUtil: FAuthUtil) {

    private var _userId: String = ""
    private val _currentLocation = MutableLiveData(Location(null))
    private var _locationsApproved = mutableListOf<Long>()
    private var _pointsOfInterestApproved = mutableListOf<Long>()
    private var _categoriesApproved = mutableListOf<Long>()
    private var _pointsOfInterestClassified = mutableMapOf<Long, Double>()

    val userId: String
        get() = _userId

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val locationsApproved: List<Long>
        get() = _locationsApproved.toList()

    val pointsOfInterestApproved: List<Long>
        get() = _pointsOfInterestApproved.toList()

    val categoriesApproved: List<Long>
        get() = _categoriesApproved.toList()

    val pointsOfInterestClassified: Map<Long, Double>
        get() = _pointsOfInterestClassified.toMap()

    /* ------------------------  Login and register (Start) ------------------------ */

    fun register(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        fAuthUtil.createUserWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun login(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        fAuthUtil.signInWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun signOut() {
        fAuthUtil.signOut()
    }

    fun updateUserId() {
        _userId = fAuthUtil.userId
    }
    /* ------------------------  Login and register (End) ------------------------ */

    /* ------------------------  Device location (Start) ------------------------ */
    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }
    /* ------------------------  Device location (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun addLocationApproved(locationId: Long) {
        _locationsApproved.add(locationId)
    }

    fun removeLocationApproved(locationId: Long) {
        _locationsApproved.remove(locationId)
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addPointOfInterestApproved(pointOfInterestId: Long) {
        _pointsOfInterestApproved.add(pointOfInterestId)
    }

    fun removePointOfInterestApproved(pointOfInterestId: Long) {
        _pointsOfInterestApproved.remove(pointOfInterestId)
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addCategoryApproved(categoryId: Long) {
        _categoriesApproved.add(categoryId)
    }

    fun removeCategoryApproved(categoryId: Long) {
        _categoriesApproved.add(categoryId)
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addPointOfInterestClassified(pointOfInterestId: Long, classification: Double) {
        _pointsOfInterestClassified[pointOfInterestId] = classification
    }

    fun removePointOfInterestClassified(pointOfInterestId: Long) {
        _pointsOfInterestClassified.remove(pointOfInterestId)
    }
    /* ------------------------  Point classification (End) ------------------------ */
}