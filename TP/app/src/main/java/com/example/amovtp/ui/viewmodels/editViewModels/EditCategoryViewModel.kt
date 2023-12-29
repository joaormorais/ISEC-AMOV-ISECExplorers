package com.example.amovtp.ui.viewmodels.editViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class EditCategoryViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCategoryViewModel(geoData, userData) as T
    }
}

class EditCategoryViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    fun getCurrentCategory(currentCategory: String): Category? {
        return geoData.categories.value.find { it.name == currentCategory }
    }

    fun isEditCategoryValid(
        name: String,
        description: String,
        fillNameError: String,
        fillDescriptionError: String,
        errorMessage: (String) -> Unit
    ) {

        if (name.isBlank())
            errorMessage(fillNameError)
        else if (description.isBlank())
            errorMessage(fillDescriptionError)

        errorMessage("")
    }

    fun editCategory(
        currentName: String,
        name: String,
        description: String
    ): String {
        val tempCategories = geoData.categories
        val tempUserId = userData.localUser.value.userId

        if (tempCategories.value.any { it.name == name })
            return Consts.ERROR_EXISTING_NAME
        else if (tempUserId.isBlank())
            return Consts.ERROR_NEED_LOGIN

        geoData.editCategory(currentName, name, description)
        geoData.updateCategory(
            currentName,
            name
        ) // TODO: meter aqui parenteses e mandar o erro para a UI (return erro)

        return Consts.SUCCESS
    }

}