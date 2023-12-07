package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.LocationHandler

class MyApplication : Application() {

    //TODO: inicializar o fiirestore

    val geoData by lazy { GeoData(/*firestore*/) }
    val usersData by lazy { UsersData(/*firestore*/) }

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

}