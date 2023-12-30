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
        onResult: (String) -> Unit
    ){
        val tempCategories = geoData.categories
        val currentUserId = userData.localUser.value.userId

        if (tempCategories.value.any { it.name == name }) {
            onResult(Consts.ERROR_EXISTING_NAME)
            return
        }else if (currentUserId.isBlank()) {
            onResult(Consts.ERROR_NEED_LOGIN)
            return
        }
        geoData.addCategory(currentUserId, name, description, img)
        {exception ->
            val message = if (exception == null) Consts.SUCCESS else exception.toString()
            onResult(message)
        }

    }
}