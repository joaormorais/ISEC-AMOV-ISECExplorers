package com.example.amovtp.ui.viewmodels.infoViewModels

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
    fun getCategories(): List<Category> {
        return geoData.categories
    }

    fun findVoteForApprovedCategoryByUser(categoryId: Int): Boolean {
        return userData.categoriesApproved.any { it == categoryId }
    }

    fun voteForApprovalCategoryByUser(categoryId: Int) {
        geoData.voteForApprovalCategory(categoryId)
        userData.addCategoryApproved(categoryId)
        if (geoData.categories.find { it.id == categoryId }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL )
            geoData.approveCategory(categoryId)
    }

    fun removeVoteForApprovalCategoryByUser(categoryId: Int) {
        geoData.removeVoteForApprovalCategory(categoryId)
        userData.removeCategoryApproved(categoryId)
    }

}