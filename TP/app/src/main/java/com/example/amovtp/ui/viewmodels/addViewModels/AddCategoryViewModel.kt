package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData

class AddCategoryViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddCategoryViewModel(geoData) as T
    }
}


class AddCategoryViewModel(private val geoData: GeoData) : ViewModel() {

}