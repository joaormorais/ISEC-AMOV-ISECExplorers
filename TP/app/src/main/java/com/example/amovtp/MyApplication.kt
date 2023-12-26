package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.services.FirebaseUserDataService
import com.example.amovtp.services.FirebaseGeoDataService
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.LocationHandler

class MyApplication : Application() {

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    private val firebaseUserDataService: FirebaseUserDataService by lazy { FirebaseUserDataService() }
    private val firebaseGeoDataService: FirebaseGeoDataService by lazy { FirebaseGeoDataService() }

    val geoData by lazy { GeoData(firebaseGeoDataService) }
    val userData by lazy { UserData(firebaseUserDataService) }

}