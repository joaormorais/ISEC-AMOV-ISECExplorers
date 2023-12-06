package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData

class AddPointOfInterestViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPointOfInterestViewModel(geoData) as T
    }
}

class AddPointOfInterestViewModel(private val geoData: GeoData) : ViewModel() {

}