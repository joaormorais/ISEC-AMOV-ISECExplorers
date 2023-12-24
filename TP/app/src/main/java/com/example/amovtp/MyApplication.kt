package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.services.FAuthService
import com.example.amovtp.services.FStorageService
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.LocationHandler

class MyApplication : Application() {

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    private val fAuthService: FAuthService by lazy { FAuthService() }
    private val fStorageService: FStorageService by lazy { FStorageService() }

    val geoData by lazy { GeoData(fStorageService) }
    val userData by lazy { UserData(fAuthService) }

}