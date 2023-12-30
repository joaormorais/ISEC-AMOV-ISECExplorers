package com.example.amovtp.ui.viewmodels.addViewModels

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class AddPointOfInterestViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPointOfInterestViewModel(geoData, userData) as T
    }
}

class AddPointOfInterestViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    fun getCurrentLocation(): MutableLiveData<Location> {
        return userData.currentLocation
    }

    fun getLocations(): MutableState<List<com.example.amovtp.data.Location>> {
        return geoData.locations
    }

    fun getCategories(): MutableState<List<Category>> {
        return geoData.categories
    }

    fun addPointOfInterest(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        location: MutableList<String>,
        category: String,
        imgs: List<String>,
        onResult: (String) -> Unit
    ){
        val tempPointsOfInterest = geoData.pointsOfInterest
        val currentUserId = userData.localUser.value.userId

        if (tempPointsOfInterest.value.any { it.name == name }) {
            onResult(Consts.ERROR_EXISTING_NAME)
            return
        }else if (tempPointsOfInterest.value.any { it.lat == lat && it.long == long }) {
            onResult(Consts.ERROR_EXISTING_POINT_OF_INTEREST)
            return
        }else if (currentUserId.isBlank()) {
            onResult(Consts.ERROR_NEED_LOGIN)
            return
        }

        geoData.addPointOfInterest(
            currentUserId,
            name,
            description,
            lat,
            long,
            isManualCoords,
            location,
            category,
            imgs
        )// TODO: metter aqui parenteses e mandar o erro para a UI (return erro)
        { exception ->
            val message = if (exception == null) Consts.SUCCESS else exception.toString()
            onResult(message)
        }
    }
}