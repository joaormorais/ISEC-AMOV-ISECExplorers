package com.example.amovtp.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlin.random.Random

/**
 * Represents a location, that is going to have a name, a descriptions, coordinates, and N points of interest
 */
data class Location(
    val id: Int,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val pointsOfInterest: MutableList<PointsOfInterest> = mutableListOf()
) //TODO: receber um array de imagens + boolean a dizer se é um distrito

class LocationsViewModel :
    ViewModel() {

    //TODO: ir buscar informação ao GeoInfoRepository

    companion object {
        private var _currentLocationId = 0
    }

    private val _locations = mutableListOf<Location>()
    val locations: List<Location>
        get() = _locations

    init {
        repeat(20) {
            val locationName = "Localização aleatória $it"
            val locationDescription = "Descrição para $locationName"
            val latitude = Random.nextDouble(-90.0, 90.0)
            val longitude = Random.nextDouble(-180.0, 180.0)

            _locations.add(
                Location(
                    _currentLocationId++,
                    locationName,
                    locationDescription,
                    latitude,
                    longitude
                )
            )

        }
    }

    /*fun removeLocation(
        locationName: String
    ): Boolean {

        // checks if the location already exists
        for (i in _locations) {
            if (i.name == locationName)
                _locations.remove(i) // removes the location
            return true
        }

        return false

    }*/

}