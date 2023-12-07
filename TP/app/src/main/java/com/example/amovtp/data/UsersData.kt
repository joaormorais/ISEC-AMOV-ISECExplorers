package com.example.amovtp.data

import androidx.compose.runtime.mutableStateOf

class UsersData(/*firestore*/) {

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")

    fun getUsername(): String {
        return _username.toString()
    }

    fun setUsername(newValue: String) {
        _username.value = newValue
    }

    fun setPassword(newValue: String) {
        _password.value = newValue
    }

}