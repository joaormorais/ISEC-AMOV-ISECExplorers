package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class CategoriesViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(geoData, userData) as T
    }
}

class CategoriesViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    /**
     * Gets every category
     */
    fun getCategories(): MutableState<List<Category>> {
        return geoData.categories
    }

    fun findVoteForApprovedCategoryByUser(categoryName: String): Boolean {
        return userData.categoriesApproved.value.any { it == categoryName }
    }

    fun voteForApprovalCategoryByUser(categoryName: String) {
        geoData.voteForApprovalCategory(categoryName)
        userData.addCategoryApproved(categoryName)
        if (geoData.categories.value.find { it.name == categoryName }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL )
            geoData.approveCategory(categoryName)
    }

    fun removeVoteForApprovalCategoryByUser(categoryName: String) {
        geoData.removeVoteForApprovalCategory(categoryName)
        userData.removeCategoryApproved(categoryName)
    }

}