package com.example.amovtp.ui.viewmodels.addViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class AddCategoryViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddCategoryViewModel(geoData, userData) as T
    }
}


class AddCategoryViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {
    fun addCategory(
        name: String,
        description: String,
        img: String,
    ): String {
        val tempCategories = geoData.categories
        val tempUserId = userData.userId

        if (tempCategories.value.any { it.name == name })
            return Consts.ERROR_EXISTING_NAME
        else if (tempUserId.isBlank())
            return Consts.ERROR_NEED_LOGIN

        geoData.addCategory(tempUserId, name, description, img)

        return Consts.SUCCESS
    }
}