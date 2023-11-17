package com.example.amovtp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


data class Category(val name: String, val locations: MutableList<Location> = mutableListOf())
data class Location(
    val name: String,
    val pointsOfInterest: MutableList<PointsOfInterest> = mutableListOf()
)

data class PointsOfInterest(val name: String)

class InfoViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories


    //TODO: funções para add categorias, localizações, e locais de interesse

}