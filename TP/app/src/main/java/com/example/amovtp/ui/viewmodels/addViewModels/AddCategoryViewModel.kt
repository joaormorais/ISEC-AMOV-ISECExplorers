package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.utils.Consts

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
    ):String{
        val tempCategories = geoData.categories

        if(tempCategories.any{it.name == name})
            return Consts.ERROR_EXISTING_NAME

        geoData.addCategory(name,description,img)

        return Consts.SUCCESS
    }
}