package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location

class LocationsViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(geoData) as T
    }
}

class LocationsViewModel(private val geoData: GeoData) : ViewModel() {

    fun getLocations(): List<Location>{
        return geoData.getLocations()
    }

    fun getLocationsOrderedByName() : List<Location>{
        return geoData.getLocations().sortedBy { it.name }
    }

    fun setPointLocationSearch(name: String) {
        geoData.setPointLocationSearch(name)
    }

    //TODO: fazer getLocationsOrderedByDistance()

}