package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.LocalUser
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

    fun getLocalUser(): MutableState<LocalUser> {
        return userData.localUser
    }

    fun getCategories(): MutableState<List<Category>> {
        return geoData.categories
    }

    fun findVoteForApprovedCategoryByUser(categoryId: String): Boolean {
        return userData.localUser.value.categoriesApproved.any { it == categoryId }
    }

    fun voteForApprovalCategoryByUser(categoryId: String) {
        geoData.voteForApprovalCategory(categoryId)
        userData.addCategoryApproved(categoryId)
        if (geoData.categories.value.find { it.id == categoryId }?.votesForApproval!! >= Consts.VOTES_NEEDED_FOR_APPROVAL )
            geoData.approveCategory(categoryId)
        geoData.updateCategory(categoryId)
        userData.updateLocalUser()
    }

    fun removeVoteForApprovalCategoryByUser(categoryId: String) {
        geoData.removeVoteForApprovalCategory(categoryId)
        userData.removeCategoryApproved(categoryId)
        geoData.updateCategory(categoryId)
        userData.updateLocalUser()
    }

}