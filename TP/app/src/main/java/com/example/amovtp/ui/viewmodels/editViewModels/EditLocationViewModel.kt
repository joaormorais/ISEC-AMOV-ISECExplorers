package com.example.amovtp.ui.viewmodels.editViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class EditLocationViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditLocationViewModel(geoData, userData) as T
    }
}

class EditLocationViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    fun getCurrentEditingLocation(currentLocation: String): Location? {
        return geoData.locations.value.find { it.id == currentLocation }
    }

    fun isEditLocationValid(
        name: String,
        description: String,
        lat: Double?,
        long: Double?,
        isManualCoords: Boolean,
        fillNameError: String,
        fillDescriptionError: String,
        fillCoordinatesError: String,
        errorMessage: (String) -> Unit
    ): Boolean {
        if (name.isBlank()) {
            errorMessage(fillNameError)
            return false
        }
        if (description.isBlank()) {
            errorMessage(fillDescriptionError)
            return false
        }
        if (!isManualCoords && (lat == null && long == null)) {
            errorMessage(fillCoordinatesError)
            return false
        }
        return true
    }

    fun editLocation(
        locationId: String,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        onResult: (String) -> Unit
    ) {
        val currentUserId = userData.localUser.value.userId
        val filteredLocations = geoData.locations.value.filter { it.id != locationId }

        if (filteredLocations.any { it.name == name })
            onResult(Consts.ERROR_EXISTING_NAME)
        else if (filteredLocations.any { it.lat == lat && it.long == long })
            onResult(Consts.ERROR_EXISTING_LOCATION)
        else if (currentUserId.isBlank())
            onResult(Consts.ERROR_NEED_LOGIN)
        else {
            val currentLocationName = geoData.locations.value.find { it.id == locationId }?.name
            geoData.pointsOfInterest.value.forEachIndexed { index, pointOfInterest ->
                if (pointOfInterest.locations.contains(currentLocationName)) {
                    geoData.changeLocationOfPoint(pointOfInterest.id, currentLocationName!!, name)
                    geoData.updatePointOfInterest(pointOfInterest.id)
                }
            }

            geoData.editLocation(locationId, name, description, lat, long, isManualCoords)
            geoData.updateLocation(locationId)
            userData.removeVotesApprovalForLocation(locationId)
            userData.updateLocalUser()
            onResult(Consts.SUCCESS)
        }
    }

}
