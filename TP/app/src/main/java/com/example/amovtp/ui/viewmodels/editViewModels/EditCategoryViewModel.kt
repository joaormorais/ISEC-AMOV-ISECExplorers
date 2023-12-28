package com.example.amovtp.ui.viewmodels.editViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData

class EditCategoryViewModelFactory(
    private val geoData: GeoData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCategoryViewModel(geoData) as T
    }
}

class EditCategoryViewModel(
    private val geoData: GeoData
) : ViewModel() {

}