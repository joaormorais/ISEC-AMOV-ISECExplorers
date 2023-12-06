package com.example.amovtp

import android.app.Application
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.UsersData

class MyApplication : Application() {

    //TODO: inicializar o fiirestore

    val geoData by lazy { GeoData(/*firestore*/) }
    val usersData by lazy { UsersData(/*firestore*/) }

}