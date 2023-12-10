package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.utils.codes.Codes

class AddLocationViewModelFactory(private val geoData: GeoData) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddLocationViewModel(geoData) as T
    }
}


class AddLocationViewModel(private val geoData: GeoData) : ViewModel() {

    fun addLocation(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        imgs: MutableList<String>
    ): Codes {

        val tempLocations = geoData.getLocations()

        if (tempLocations.any { it.name == name })
            return Codes.ERROR_EXISTING_NAME
        else if (tempLocations.any { it.lat == lat })
            return Codes.ERROR_EXISTING_LAT
        else if (tempLocations.any { it.long == long })
            return Codes.ERROR_EXISTING_LONG

        geoData.addLocation(name, description, lat, long, imgs)

        return Codes.SUCCESS
    }

}