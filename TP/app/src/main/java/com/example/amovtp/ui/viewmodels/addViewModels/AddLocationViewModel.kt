package com.example.amovtp.ui.viewmodels.addViewModels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData
import com.example.amovtp.utils.Consts

class AddLocationViewModelFactory(
    private val geoData: GeoData,
    private val usersData: UsersData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddLocationViewModel(geoData, usersData) as T
    }
}


class AddLocationViewModel(
    private val geoData: GeoData,
    private val usersData: UsersData
) : ViewModel() {

    fun getCurrentLocation(): MutableLiveData<Location> {
        return usersData.currentLocation
    }

    fun addLocation(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        imgs: List<String>
    ): String {

        val tempLocations = geoData.locations

        if (tempLocations.any { it.name == name })
            return Consts.ERROR_EXISTING_NAME
        else if (tempLocations.any { it.lat == lat && it.long == long })
            return Consts.ERROR_EXISTING_LOCATION

        geoData.addLocation(name, description, lat, long, isManualCoords, imgs)

        return Consts.SUCCESS
    }

}