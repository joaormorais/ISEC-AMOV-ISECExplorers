package com.example.amovtp.ui.viewmodels.addViewModels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData
import com.example.amovtp.utils.codes.Codes

class AddPointOfInterestViewModelFactory(
    private val geoData: GeoData,
    private val usersData: UsersData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPointOfInterestViewModel(geoData, usersData) as T
    }
}

class AddPointOfInterestViewModel(
    private val geoData: GeoData,
    private val usersData: UsersData
) : ViewModel() {

    fun getCurrentLocation(): MutableLiveData<Location> {
        return usersData.getCurrentLocation()
    }

    fun getLocations(): List<com.example.amovtp.data.Location> {
        return geoData.getLocations()
    }

    fun getCategories() : List<Category>{
        return geoData.getCategories()
    }

    fun addPointOfInterest(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        location: String,
        category: String,
        imgs: List<String>
    ): Codes {
        val tempPointsOfInterest = geoData.getPointsOfInterest()

        if (tempPointsOfInterest.any { it.name == name })
            return Codes.ERROR_EXISTING_NAME
        else if (tempPointsOfInterest.any { it.lat == lat && it.long == long })
            return Codes.ERROR_EXISTING_POINT_OF_INTEREST

        geoData.addPointOfInterest(
            name,
            description,
            lat,
            long,
            isManualCoords,
            location,
            category,
            imgs
        )

        return Codes.SUCCESS
    }
}