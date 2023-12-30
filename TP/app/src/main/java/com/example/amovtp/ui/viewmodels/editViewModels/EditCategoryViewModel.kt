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

    fun getCurrentEditingCategory(currentCategory: String): Category? {
        return geoData.categories.value.find { it.id == currentCategory }
    }

    fun isEditCategoryValid(
        name: String,
        description: String,
        fillNameError: String,
        fillDescriptionError: String,
        errorMessage: (String) -> Unit
    ): Boolean {
        if (name.isBlank()) {
            errorMessage(fillNameError)
            return false
        } else if (description.isBlank()) {
            errorMessage(fillDescriptionError)
            return false
        }
        return true
    }

    fun editCategory(
        categoryId: String,
        name: String,
        description: String,
        onResult: (String) -> Unit
    ) {
        val currentUserId = userData.localUser.value.userId
        val filteredCategories = geoData.categories.value.filter { it.id != categoryId }

        if (filteredCategories.any { it.name == name })
            onResult(Consts.ERROR_EXISTING_NAME)
        else if (currentUserId.isBlank())
            onResult(Consts.ERROR_NEED_LOGIN)
        else {
            val currentCategoryName = geoData.categories.value.find { it.id == categoryId }?.name
            geoData.pointsOfInterest.value.forEachIndexed { index, pointOfInterest ->
                if(pointOfInterest.category == currentCategoryName){
                    geoData.changeCategoryOfPoint(pointOfInterest.id,name)
                    geoData.updatePointOfInterest(pointOfInterest.id)
                }
            }

            geoData.editCategory(categoryId, name, description)
            geoData.updateCategory(categoryId)
            userData.removeVotesApprovalForCategory(categoryId)
            userData.updateLocalUser()
            onResult(Consts.SUCCESS)
        }
    }

}