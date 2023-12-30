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
        val currentUserId = userData.localUser.value.userId

        if (tempLocations.value.any { it.name == name }) {
            onResult(Consts.ERROR_EXISTING_NAME)
        }else if (tempLocations.value.any { it.lat == lat && it.long == long }) {
            onResult(Consts.ERROR_EXISTING_LOCATION)
        }else if (currentUserId.isBlank()) {
            onResult(Consts.ERROR_NEED_LOGIN)
        }else{
            geoData.addLocation(currentUserId, name, description, lat, long, isManualCoords, imgs)
            {exception ->
                val message = if (exception == null) Consts.SUCCESS else exception.toString()
                onResult(message)
            }
        }
    }

    fun isAddLocationValid(
        name: String,
        description: String,
        lat: Double?,
        long: Double?,
        isManualCoords: Boolean,
        imgsGallery: List<String>,
        imgsCamera: List<String>,
        fillNameError : String,
        fillDescriptionError : String,
        fillCoordinatesError : String,
        fillImagesError: String,
        errorMessage: (String) -> Unit
    ): Boolean {
        if(name.isBlank()){
            errorMessage(fillNameError)
            return false
        }
        if (description.isBlank()){
            errorMessage(fillDescriptionError)
            return false
        }
        if(isManualCoords && (lat == null || long == null)){
            errorMessage(fillCoordinatesError)
            return false
        }
        if(imgsGallery.isEmpty() && imgsCamera.isEmpty()){
            errorMessage(fillImagesError)
            return false
        }
        return true
    }

}