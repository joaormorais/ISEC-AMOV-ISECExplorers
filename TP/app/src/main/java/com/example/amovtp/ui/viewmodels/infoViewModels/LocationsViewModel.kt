package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

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
        var latDistance = Math.toRadians(locLat - currLat)
        var longDistance = Math.toRadians(locLong - currLong)

        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(currLat)) * Math.cos(Math.toRadians(locLat)) *
                Math.sin(longDistance / 2) * Math.sin(longDistance / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c

    }

    fun getLocations(): List<Location> {
        return geoData.locations
    }

    fun getLocationsOrderedByName(): List<Location> {
        return geoData.locations.sortedBy { it.name }
    }

    fun getLocationsOrderedByDistance(): List<Location> {

        val currentLocation = userData.currentLocation

        return geoData.locations.sortedBy { location ->

            calculateDistance(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude,
                location.lat,
                location.long
            )

        }
    }

    fun findVoteForApprovedLocationByUser(locationId: Long): Boolean {
        return userData.locationsApproved.any { it == locationId }
    }

    fun voteForApprovalLocationByUser(locationId: Long) {
        geoData.voteForApprovalLocation(locationId)
        userData.addLocationApproved(locationId)
        if (geoData.locations.find { it.id == locationId }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL )
            geoData.approveLocation(locationId)
    }

    fun removeVoteForApprovalLocationByUser(locationId: Long) {
        geoData.removeVoteForApprovalLocation(locationId)
        userData.removeLocationApproved(locationId)
    }

}