package com.example.amovtp.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UsersData
import pt.isec.ans.locationmaps.utils.LocationHandler

@Suppress("UNCHECKED_CAST")
class GPSViewModelFactory(
    private val locationHandler: LocationHandler,
    private val usersData: UsersData
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GPSViewModel(locationHandler, usersData) as T
    }
}

class GPSViewModel(
    private val locationHandler: LocationHandler,
    usersData: UsersData
) : ViewModel() {

    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false

    init {
        locationHandler.onLocation = {
            usersData.setCurrentLocation(it)
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