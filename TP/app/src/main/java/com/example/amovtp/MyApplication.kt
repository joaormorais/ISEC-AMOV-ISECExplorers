package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.fb.FAuthUtil
import com.example.amovtp.utils.fb.FStorageUtil
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.LocationHandler

class MyApplication : Application() {

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    val fAuthUtil: FAuthUtil by lazy { FAuthUtil() }
    val fStorageUtil: FStorageUtil by lazy { FStorageUtil() }

    val geoData by lazy { GeoData(/*fstorageutil*/) }
    val userData by lazy { UserData(fAuthUtil) }

}