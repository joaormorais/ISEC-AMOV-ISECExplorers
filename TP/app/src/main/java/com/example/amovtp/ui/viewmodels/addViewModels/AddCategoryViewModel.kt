package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.utils.codes.Codes

class AddCategoryViewModelFactory(private val geoData: GeoData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddCategoryViewModel(geoData) as T
    }
}


class AddCategoryViewModel(private val geoData: GeoData) : ViewModel() {
    fun addCategory(
        name: String,
        description: String,
        img: String,
    ):Codes{
        val tempCategories = geoData.getCategories()

        if(tempCategories.any{it.name == name})
            return Codes.ERROR_EXISTING_NAME

        geoData.addCategory(name,description,img)

        return Codes.SUCCESS
    }
}