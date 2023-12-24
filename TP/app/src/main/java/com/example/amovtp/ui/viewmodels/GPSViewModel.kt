package com.example.amovtp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UserData
import pt.isec.ans.locationmaps.utils.LocationHandler

@Suppress("UNCHECKED_CAST")
class GPSViewModelFactory(
    private val locationHandler: LocationHandler,
    private val userData: UserData
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GPSViewModel(locationHandler, userData) as T
    }
}

class GPSViewModel(
    private val locationHandler: LocationHandler,
    userData: UserData
) : ViewModel() {

    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false

    init {
        locationHandler.onLocation = {
            userData.setCurrentLocation(it)
        }
    }

    fun startLocationUpdates() {
        if (fineLocationPermission && coarseLocationPermission) {
            locationHandler.startLocationUpdates()
        }

    }

    fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}