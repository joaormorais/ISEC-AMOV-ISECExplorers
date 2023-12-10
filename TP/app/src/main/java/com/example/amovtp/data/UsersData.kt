package com.example.amovtp.data

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UsersData(/*firestore*/) {

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")
    private val _currentLocation = MutableLiveData(Location(null))

    fun getUsername(): String {
        return _username.toString()
    }

    fun setUsername(newValue: String) {
        _username.value = newValue
    }

    fun setPassword(newValue: String) {
        _password.value = newValue
    }

    fun getCurrentLocation(): MutableLiveData<Location> {
        return _currentLocation
    }

    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }

}