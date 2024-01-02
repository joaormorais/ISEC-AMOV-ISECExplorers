package com.example.amovtp.data

import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.amovtp.services.FirebaseUserDataService

data class LocalUser(
    var userId: String = "",
    var locationsApproved: List<String> = emptyList(),
    var pointsOfInterestApproved: List<String> = emptyList(),
    var categoriesApproved: List<String> = emptyList(),
    var pointsOfInterestClassified: Map<String, Long> = emptyMap()
)

class UserData(private val firebaseUserDataService: FirebaseUserDataService) {

    private val _currentLocation = MutableLiveData(Location(null))
    private val _localUser = mutableStateOf(LocalUser())

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    val localUser: MutableState<LocalUser>
        get() = _localUser

    /* ------------------------  Login, register and update (Start) ------------------------ */

    fun register(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        firebaseUserDataService.createUserWithEmail(email, pw) { exception ->
            onResult(exception)
        }
        _localUser.value = LocalUser()
    }

    fun login(email: String, pw: String, onResult: (Throwable?) -> Unit) {
        firebaseUserDataService.signInWithEmail(email, pw) { exception ->
            onResult(exception)

            if(exception==null){
                firebaseUserDataService.startObserverGeoData(
                    onFoundUser = { foundUser ->
                        if (foundUser.isNotEmpty()) {
                            _localUser.value = LocalUser(
                                userId = foundUser["userId"] as String,
                                locationsApproved = foundUser["locationsApproved"] as List<String>,
                                pointsOfInterestApproved = foundUser["pointsOfInterestApproved"] as List<String>,
                                categoriesApproved = foundUser["categoriesApproved"] as List<String>,
                                pointsOfInterestClassified = foundUser["pointsOfInterestClassified"] as Map<String, Long>,
                            )
                            Log.d("UserData", "_localUser.value = " + _localUser.value)
                        }
                    }
                )
            }
        }
    }

    fun signOut() {
        firebaseUserDataService.signOut()
        _localUser.value = LocalUser()
        firebaseUserDataService.stopObserver()
    }

    fun updateUserId() {
        _localUser.value.userId = firebaseUserDataService.userId
    }

    fun createUser() {
        updateUserId()
        firebaseUserDataService.addLocalUserToFirestore(_localUser.value) {}
        _localUser.value.userId = ""
    }

    fun updateLocalUser() {
        firebaseUserDataService.updateLocalUserToFirestore(_localUser.value) {
        }
    }

    /* ------------------------  Login, register and update (End) ------------------------ */

    /* ------------------------  Remove votes (Start) ------------------------ */

    fun removeVotesApprovalForLocation(locationId:String){
        _localUser.value.locationsApproved = _localUser.value.locationsApproved - locationId
    }

    fun removeVotesApprovalForPointOfInterest(pointOfInterestId:String){
        _localUser.value.pointsOfInterestApproved = _localUser.value.pointsOfInterestApproved - pointOfInterestId
    }

    fun removeVotesApprovalForCategory(categoryId:String){
        _localUser.value.categoriesApproved = _localUser.value.categoriesApproved - categoryId
    }

    /* ------------------------  Remove votes (End) ------------------------ */

    /* ------------------------  Device location (Start) ------------------------ */
    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }
    /* ------------------------  Device location (End) ------------------------ */

    /* ------------------------  Location approval (Start) ------------------------ */
    fun addLocationApproved(locationName: String) {
        _localUser.value.locationsApproved = _localUser.value.locationsApproved + locationName
    }

    fun removeLocationApproved(locationName: String) {
        _localUser.value.locationsApproved = _localUser.value.locationsApproved - locationName
    }
    /* ------------------------  Location approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addPointOfInterestApproved(pointOfInterestName: String) {
        _localUser.value.pointsOfInterestApproved =
            _localUser.value.pointsOfInterestApproved + pointOfInterestName
    }

    fun removePointOfInterestApproved(pointOfInterestName: String) {
        _localUser.value.pointsOfInterestApproved =
            _localUser.value.pointsOfInterestApproved - pointOfInterestName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point of interest approval (Start) ------------------------ */
    fun addCategoryApproved(categoryName: String) {
        _localUser.value.categoriesApproved = _localUser.value.categoriesApproved + categoryName
    }

    fun removeCategoryApproved(categoryName: String) {
        _localUser.value.categoriesApproved = _localUser.value.categoriesApproved - categoryName
    }
    /* ------------------------  Point of interest approval (End) ------------------------ */

    /* ------------------------  Point classification (Start) ------------------------ */
    fun addPointOfInterestClassified(pointOfInterestName: String, classification: Long) {
        _localUser.value.pointsOfInterestClassified =
            _localUser.value.pointsOfInterestClassified + (pointOfInterestName to classification)
    }

    fun removePointOfInterestClassified(pointOfInterestName: String) {
        _localUser.value.pointsOfInterestClassified =
            _localUser.value.pointsOfInterestClassified.filterNot { it.key == pointOfInterestName }
    }
    /* ------------------------  Point classification (End) ------------------------ */
}