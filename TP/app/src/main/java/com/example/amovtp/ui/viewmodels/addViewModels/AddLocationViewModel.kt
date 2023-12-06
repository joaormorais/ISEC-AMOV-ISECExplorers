package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData

class AddLocationViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddLocationViewModel(geoData) as T
    }
}


class AddLocationViewModel(private val geoData: GeoData) : ViewModel() {

}