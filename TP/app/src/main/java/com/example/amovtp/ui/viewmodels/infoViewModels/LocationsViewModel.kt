package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.LocalUser
import com.example.amovtp.data.Location
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationsViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(geoData, userData) as T
    }
}

class LocationsViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    /**
     * Calculates the distance between a point and the current location of the device
     */
    private fun calculateDistance(
        currLat: Double, // lat1
        currLong: Double, // long1
        locLat: Double, // lat2
        locLong: Double // long2
    ): Double {

        val earthRadius = 6371
        val latDistance = Math.toRadians(locLat - currLat)
        val longDistance = Math.toRadians(locLong - currLong)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(currLat)) * cos(Math.toRadians(locLat)) *
                sin(longDistance / 2) * sin(longDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c

    }

    fun getLocalUser(): MutableState<LocalUser> {
        return userData.localUser
    }

    fun getLocations(): MutableState<List<Location>> {
        return geoData.locations
    }

    fun getLocationsOrderedByDistance(): List<Location> {

        val currentLocation = userData.currentLocation

        return geoData.locations.value.sortedBy { location ->
            calculateDistance(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude,
                location.lat,
                location.long
            )
        }
    }

    fun findVoteForApprovedLocationByUser(locationName: String): Boolean {
        return userData.localUser.value.locationsApproved.any { it == locationName }
    }

    fun voteForApprovalLocationByUser(locationName: String) {
        geoData.voteForApprovalLocation(locationName)
        userData.addLocationApproved(locationName)
        if (geoData.locations.value.find { it.name == locationName }?.votesForApproval!! >= Consts.VOTES_NEEDED_FOR_APPROVAL)
            geoData.approveLocation(locationName)

        geoData.editLocation(locationName)
        userData.editLocalUser()
    }

    fun removeVoteForApprovalLocationByUser(locationName: String) {
        geoData.removeVoteForApprovalLocation(locationName)
        userData.removeLocationApproved(locationName)
        geoData.editLocation(locationName)
        userData.editLocalUser()
    }

}