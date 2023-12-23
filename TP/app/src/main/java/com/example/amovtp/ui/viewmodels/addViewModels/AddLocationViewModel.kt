package com.example.amovtp.ui.viewmodels.addViewModels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class AddLocationViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddLocationViewModel(geoData, userData) as T
    }
}


class AddLocationViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    fun getCurrentLocation(): MutableLiveData<Location> {
        return userData.currentLocation
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
        val tempUserId = userData.userId

        if (tempLocations.any { it.name == name })
            return Consts.ERROR_EXISTING_NAME
        else if (tempLocations.any { it.lat == lat && it.long == long })
            return Consts.ERROR_EXISTING_LOCATION
        else if(tempUserId==null)
            return Consts.ERROR_NEED_LOGIN

        geoData.addLocation(name,tempUserId, description, lat, long, isManualCoords, imgs)

        return Consts.SUCCESS
    }

}