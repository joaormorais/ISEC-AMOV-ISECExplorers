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
        imgs: List<String>,
        onResult: (String) -> Unit
    ){

        val tempLocations = geoData.locations
        val tempUserId = userData.localUser.value.userId

        if (tempLocations.value.any { it.name == name }) {
            onResult(Consts.ERROR_EXISTING_NAME)
            return
        }else if (tempLocations.value.any { it.lat == lat && it.long == long }) {
            onResult(Consts.ERROR_EXISTING_LOCATION)
            return
        }else if (tempUserId.isBlank()) {
            onResult(Consts.ERROR_NEED_LOGIN)
            return
        }
        geoData.addLocation(tempUserId, name, description, lat, long, isManualCoords, imgs) // TODO: metter aqui parenteses e mandar o erro para a UI (return erro)
        {exception ->
            val message = if (exception == null) Consts.SUCCESS else exception.toString()
            onResult(message)
        }
    }

}