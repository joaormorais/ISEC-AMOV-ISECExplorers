package com.example.amovtp.ui.viewmodels.addViewModels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData
import com.example.amovtp.utils.codes.Codes

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
        return usersData.getCurrentLocation()
    }

    fun addLocation(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        imgs: List<String>
    ): Codes {

        val tempLocations = geoData.getLocations()

        if (tempLocations.any { it.name == name })
            return Codes.ERROR_EXISTING_NAME
        else if (tempLocations.any { it.lat == lat && it.long == long })
            return Codes.ERROR_EXISTING_LOCATION

        geoData.addLocation(name, description, lat, long, isManualCoords, imgs)

        return Codes.SUCCESS
    }

}