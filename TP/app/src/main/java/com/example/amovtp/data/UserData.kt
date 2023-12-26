package com.example.amovtp.data

import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.amovtp.services.FirebaseUserDataService

data class SavedVotes(
    var userId: String = "",
    var locationsApproved: List<String> = emptyList(),
    var pointsOfInterestApproved: List<String> = emptyList(),
    var categoriesApproved: List<String> = emptyList(),
    var pointsOfInterestClassified: Map<String, Double> = emptyMap()
)

class UserData(private val firebaseUserDataService: FirebaseUserDataService) {

    private val _currentLocation = MutableLiveData(Location(null))
    private val _savedVotes = mutableStateOf(SavedVotes())

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val savedVotes: MutableState<SavedVotes>
        get() = _savedVotes

    /* ------------------------  Login and register (Start) ------------------------ */

    fun register(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        firebaseUserDataService.createUserWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun login(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        firebaseUserDataService.signInWithEmail(email, pw) { exception ->
            onResult(exception)
        }
    }

    fun signOut() {
        firebaseUserDataService.signOut()
    }

    fun updateUserId() {
        _savedVotes.value.userId = firebaseUserDataService.userId
    }

    fun resetUserId(){
        _savedVotes.value.userId = ""
    }

    fun createUser(){
        firebaseUserDataService.addSavedVotesToFirestore(_savedVotes.value) {}
        firebaseUserDataService.clearUserId()
    }

    fun searchUser() {
        firebaseUserDataService.locateUserFirestore(
            onFoundUser = { foundUser ->
                if (foundUser.isNotEmpty()) {
                    _savedVotes.value = SavedVotes(
                        userId = foundUser["userId"] as String,
                        locationsApproved = foundUser["locationsApproved"] as List<String>,
                        pointsOfInterestApproved = foundUser["pointsOfInterestApproved"] as List<String>,
                        categoriesApproved = foundUser["categoriesApproved"] as List<String>,
                        pointsOfInterestClassified = foundUser["pointsOfInterestClassified"] as Map<String, Double>,
                    )
                }
            }
        )
    }

    /* ------------------------  Login and register (End) ------------------------ */

    /* ------------------------  Device location (Start) ------------------------ */
    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }
    /* ------------------------  Device location (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun addLocationApproved(locationName: String) {
        _savedVotes.value.locationsApproved = _savedVotes.value.locationsApproved + locationName
    }

    fun removeLocationApproved(locationName: String) {
        _savedVotes.value.locationsApproved = _savedVotes.value.locationsApproved - locationName
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addPointOfInterestApproved(pointOfInterestName: String) {
        _savedVotes.value.pointsOfInterestApproved =
            _savedVotes.value.pointsOfInterestApproved + pointOfInterestName
    }

    fun removePointOfInterestApproved(pointOfInterestName: String) {
        _savedVotes.value.pointsOfInterestApproved =
            _savedVotes.value.pointsOfInterestApproved - pointOfInterestName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addCategoryApproved(categoryName: String) {
        _savedVotes.value.categoriesApproved = _savedVotes.value.categoriesApproved + categoryName
    }

    fun removeCategoryApproved(categoryName: String) {
        _savedVotes.value.categoriesApproved = _savedVotes.value.categoriesApproved - categoryName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addPointOfInterestClassified(pointOfInterestName: String, classification: Double) {
        _savedVotes.value.pointsOfInterestClassified =
            _savedVotes.value.pointsOfInterestClassified + (pointOfInterestName to classification)
    }

    fun removePointOfInterestClassified(pointOfInterestName: String) {
        _savedVotes.value.pointsOfInterestClassified =
            _savedVotes.value.pointsOfInterestClassified.filterNot { it.key == pointOfInterestName }
    }
    /* ------------------------  Point classification (End) ------------------------ */
}