package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location

class LocationsViewModelFactory(private val geoData: GeoData) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(geoData) as T
    }
}

class LocationsViewModel(private val geoData: GeoData) : ViewModel() {

    fun getLocations(): List<Location> {
        return geoData.getLocations()
    }

    fun getLocationsOrderedByName(): List<Location> {
        return geoData.getLocations().sortedBy { it.name }
    }

    fun getLocationsOrderedByDistance(): List<Location> {

        val currentLocation = geoData.getCurrentLocation()

        //temp
        currentLocation.latitude = 40.176068569348324
        currentLocation.longitude = -8.680650875229537

        return geoData.getLocations().sortedBy { location ->

            calculateDistance(
                currentLocation.latitude,
                currentLocation.longitude,
                location.lat,
                location.long
            )

        }
    }

    fun calculateDistance(
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

}