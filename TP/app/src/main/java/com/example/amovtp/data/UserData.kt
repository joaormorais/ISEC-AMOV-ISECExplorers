package com.example.amovtp.data

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.amovtp.services.FAuthService

class UserData(private val fAuthService: FAuthService) {

    private var _userId: String = ""
    private val _currentLocation = MutableLiveData(Location(null))
    private var _locationsApproved = mutableStateOf(emptyList<String>())
    private var _pointsOfInterestApproved = mutableStateOf(emptyList<String>())
    private var _categoriesApproved = mutableStateOf(emptyList<String>())
    private var _pointsOfInterestClassified = mutableStateOf(emptyMap<String, Double>())

    val userId: String
        get() = _userId

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val locationsApproved: MutableState<List<String>>
        get() = _locationsApproved

    val pointsOfInterestApproved: MutableState<List<String>>
        get() = _pointsOfInterestApproved

    val categoriesApproved: MutableState<List<String>>
        get() = _categoriesApproved

    val pointsOfInterestClassified: MutableState<Map<String, Double>>
        get() = _pointsOfInterestClassified

    /* ------------------------  Login and register (Start) ------------------------ */

    fun register(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        fAuthService.createUserWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun login(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        fAuthService.signInWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun signOut() {
        fAuthService.signOut()
    }

    fun updateUserId() {
        _userId = fAuthService.userId
    }
    /* ------------------------  Login and register (End) ------------------------ */

    /* ------------------------  Device location (Start) ------------------------ */
    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }
    /* ------------------------  Device location (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun addLocationApproved(locationName: String) {
        _locationsApproved.value = _locationsApproved.value + locationName
    }

    fun removeLocationApproved(locationName: String) {
        _locationsApproved.value = _locationsApproved.value - locationName
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addPointOfInterestApproved(pointOfInterestName: String) {
        _pointsOfInterestApproved.value = _pointsOfInterestApproved.value + pointOfInterestName
    }

    fun removePointOfInterestApproved(pointOfInterestName: String) {
        _pointsOfInterestApproved.value = _pointsOfInterestApproved.value - pointOfInterestName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addCategoryApproved(categoryName: String) {
        _categoriesApproved.value = _categoriesApproved.value + categoryName
    }

    fun removeCategoryApproved(categoryName: String) {
        _categoriesApproved.value = _categoriesApproved.value - categoryName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addPointOfInterestClassified(pointOfInterestName: String, classification: Double) {
        _pointsOfInterestClassified.value =
            _pointsOfInterestClassified.value + (pointOfInterestName to classification)
    }

    fun removePointOfInterestClassified(pointOfInterestName: String) {
        _pointsOfInterestClassified.value =
            _pointsOfInterestClassified.value.filterNot { it.key == pointOfInterestName }
    }
    /* ------------------------  Point classification (End) ------------------------ */
}