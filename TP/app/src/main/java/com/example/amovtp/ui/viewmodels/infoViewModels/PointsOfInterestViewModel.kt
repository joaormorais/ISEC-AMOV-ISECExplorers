package com.example.amovtp.ui.viewmodels.infoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.PointOfInterest

class PointsOfInterestViewModelFactory(private val geoData: GeoData) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PointsOfInterestViewModel(geoData) as T
    }
}

class PointsOfInterestViewModel(private val geoData: GeoData) : ViewModel() {

    fun getPointsOfInterest(): List<PointOfInterest> {

        if (geoData.getPointLocationSearch().isEmpty())
            return geoData.getPointsOfInterest()
        else {
            val temp = geoData.getPointsOfInterest()
                .filter { it.locations.contains(geoData.getPointLocationSearch()) }
            geoData.setPointLocationSearch("")
            return temp
        }

    }


}