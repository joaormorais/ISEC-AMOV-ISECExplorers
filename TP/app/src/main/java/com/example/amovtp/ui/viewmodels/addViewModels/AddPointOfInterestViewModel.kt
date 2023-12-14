package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.utils.codes.Codes

class AddPointOfInterestViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPointOfInterestViewModel(geoData) as T
    }
}

class AddPointOfInterestViewModel(private val geoData: GeoData) : ViewModel() {
    fun addPointOfInterest(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        locations: MutableList<String>,
        category: String,
        imgs: MutableList<String>
    ): Codes {
        val tempPointsOfInterest = geoData.getPointsOfInterest()

        if(tempPointsOfInterest.any {it.name == name})
            return Codes.ERROR_EXISTING_NAME
        else if(tempPointsOfInterest.any{it.lat == lat && it.long == long})
            return Codes.ERROR_EXISTING_POINT_OF_INTEREST

        geoData.addPointOfInterest(name,description,lat,long,locations,category,imgs)

        return Codes.SUCCESS
    }
}