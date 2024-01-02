package com.example.amovtp.ui.viewmodels.editViewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.Consts

class EditPointOfInterestViewModelFactory(
    private val geoData: GeoData,
    private val userData: UserData
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditPointOfInterestViewModel(geoData, userData) as T
    }
}

class EditPointOfInterestViewModel(
    private val geoData: GeoData,
    private val userData: UserData
) : ViewModel() {

    fun getCurrentLocation(): MutableLiveData<android.location.Location> {
        return userData.currentLocation
    }

    fun getLocations(): MutableState<List<Location>> {
        return geoData.locations
    }

    fun getCategories(): MutableState<List<Category>> {
        return geoData.categories
    }

    fun getCurrentEditingPointOfInterest(currentPointOfInterestId: String): PointOfInterest? {
        return geoData.pointsOfInterest.value.find { it.id == currentPointOfInterestId }
    }

    fun isEditPointOfInterestValid(
        name: String,
        description: String,
        lat: Double?,
        long: Double?,
        selectedLocations: List<String>,
        selectedCategory: String,
        fillNameError: String,
        fillDescriptionError: String,
        fillCoordinatesError: String,
        fillLocationError: String,
        fillCategoryError: String,
        errorMessage: (String) -> Unit
    ): Boolean {
        if (name.isBlank()) {
            errorMessage(fillNameError)
            return false
        }
        if (description.isBlank()) {
            errorMessage(fillDescriptionError)
            return false
        }
        if (lat == null || long == null) {
            errorMessage(fillCoordinatesError)
            return false
        }
        if (selectedLocations.isEmpty()) {
            errorMessage(fillLocationError)
            return false
        }
        if (selectedCategory.isBlank()) {
            errorMessage(fillCategoryError)
            return false
        }
        return true
    }

    fun editPointOfInterest(
        pointOfInterestId: String,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        isManualCoords: Boolean,
        locations: MutableList<String>,
        category: String,
        onResult: (String) -> Unit
    ) {
        val currentUserId = userData.localUser.value.userId
        val filteredPointsOfInterest =
            geoData.pointsOfInterest.value.filter { it.id != pointOfInterestId }

        if (filteredPointsOfInterest.any { it.name == name })
            onResult(Consts.ERROR_EXISTING_NAME)
        else if (filteredPointsOfInterest.any { it.lat == lat && it.long == long })
            onResult(Consts.ERROR_EXISTING_POINT_OF_INTEREST)
        else if (currentUserId.isBlank())
            onResult(Consts.ERROR_NEED_LOGIN)
        else {

            val currentPointOfInterestName =
                geoData.pointsOfInterest.value.find { it.id == pointOfInterestId }?.name

            geoData.locations.value.forEachIndexed { _, it ->
                if (it.pointsOfInterest.contains(currentPointOfInterestName)) {

                    if (!locations.contains(it.name))
                        geoData.changePointsOfInterestArrayOfLocation(
                            it.id,
                            currentPointOfInterestName!!,
                            false
                        )
                    else
                        geoData.changePointsOfInterestNameOfLocation(
                            it.id,
                            currentPointOfInterestName!!,
                            name
                        )

                    geoData.updateLocation(it.id)
                } else {
                    if(locations.contains(it.name)){
                        geoData.changePointsOfInterestArrayOfLocation(
                            it.id,
                            name,
                            true
                        )
                        geoData.updateLocation(it.id)
                    }
                }
            }

            geoData.editPointOfInterest(
                pointOfInterestId,
                name,
                description,
                lat,
                long,
                isManualCoords,
                locations,
                category,
            )
            geoData.updatePointOfInterest(pointOfInterestId)
            userData.removeVotesApprovalForPointOfInterest(pointOfInterestId)
            userData.updateLocalUser()
            onResult(Consts.SUCCESS)
        }
    }

}