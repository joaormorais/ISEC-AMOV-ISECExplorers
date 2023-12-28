package com.example.amovtp.ui.viewmodels.usersViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UserData

class RegisterViewModelFactory(private val userData: UserData): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(userData) as T
    }
}

class RegisterViewModel(private val userData: UserData) : ViewModel() {

    fun register(email:String,pw:String,onResult: (Throwable?) -> Unit){
        userData.register(email,pw){exception ->
            if(exception==null){
                userData.createUser()
            }
            onResult(exception)
        }
    }

}