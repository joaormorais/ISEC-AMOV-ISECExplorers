package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData
import com.example.amovtp.utils.Consts

class CategoriesViewModelFactory(
    private val geoData: GeoData,
    private val usersData: UsersData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(geoData, usersData) as T
    }
}

class CategoriesViewModel(
    private val geoData: GeoData,
    private val usersData: UsersData
) : ViewModel() {

    /**
     * Gets every category
     */
    fun getCategories(): List<Category> {
        return geoData.categories
    }

    fun findVoteForApprovedCategoryByUser(categoryId: Int): Boolean {
        return usersData.categoriesApproved.any { it == categoryId }
    }

    fun voteForApprovalCategoryByUser(categoryId: Int) {
        geoData.voteForApprovalCategory(categoryId)
        usersData.addCategoryApproved(categoryId)
        if (geoData.categories.find { it.id == categoryId }?.votes!! >= Consts.VOTES_NEEDED_FOR_APPROVAL )
            geoData.approveCategory(categoryId)
    }

    fun removeVoteForApprovalCategoryByUser(categoryId: Int) {
        geoData.removeVoteForApprovalCategory(categoryId)
        usersData.removeCategoryApproved(categoryId)
    }

}