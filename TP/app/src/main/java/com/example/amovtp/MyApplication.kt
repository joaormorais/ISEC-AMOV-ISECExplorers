package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.LocationHandler

class MyApplication : Application() {

    //TODO: inicializar o fiirestore

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    val geoData by lazy { GeoData(/*firestore*/) }
    val userData by lazy { UserData(/*firestore*/) }

}