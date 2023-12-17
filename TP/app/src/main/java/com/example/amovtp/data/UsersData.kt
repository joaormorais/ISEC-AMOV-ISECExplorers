package com.example.amovtp.data

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UsersData(/*firestore*/) {

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")
    private var _locationsUpVotes = mutableListOf<Int>()
    private var _locationsDownVotes = mutableListOf<Int>()
    private var _pointsOfInterestUpVotes = mutableListOf<Int>()
    private var _pointsOfInterestDownVotes = mutableListOf<Int>()
    private val _currentLocation = MutableLiveData(Location(null))

    val username: String
        get() = _username.toString()

    val locationsUpVotes: List<Int>
        get() = _locationsUpVotes.toList()

    val locationsDownVotes: List<Int>
        get() = _locationsDownVotes.toList()

    val pointsOfInterestUpVotes: List<Int>
        get() = _pointsOfInterestUpVotes.toList()

    val pointsOfInterestDownVotes: List<Int>
        get() = _pointsOfInterestDownVotes.toList()

    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    fun setUsername(newValue: String) {
        _username.value = newValue
    }

    fun setPassword(newValue: String) {
        _password.value = newValue
    }

    fun setCurrentLocation(location: Location) {
        _currentLocation.postValue(location)
    }

    fun addLocationUpVote(vote: Int) {
        _locationsUpVotes.add(vote)
    }

    fun removeLocationUpVote(vote: Int) {
        _locationsUpVotes.remove(vote)
    }

    fun addLocationDownVote(vote: Int) {
        _locationsDownVotes.add(vote)
    }

    fun removeLocationDownVote(vote: Int) {
        _locationsDownVotes.remove(vote)
    }

    fun addPointOfInterestUpVote(vote: Int) {
        _pointsOfInterestUpVotes.add(vote)
    }

    fun removePointOfInterestUpVote(vote: Int) {
        _pointsOfInterestUpVotes.remove(vote)
    }

    fun addPointOfInterestDownVote(vote: Int) {
        _pointsOfInterestDownVotes.add(vote)
    }

    fun removePointOfInterestDownVote(vote: Int) {
        _pointsOfInterestDownVotes.remove(vote)
    }

}