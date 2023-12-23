package com.example.amovtp.ui.viewmodels.usersViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UserData

class LoginViewModelFactory(private val userData: UserData): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(userData) as T
    }
}

class LoginViewModel(private val userData: UserData) : ViewModel() {

    fun login(email:String,pw:String,onResult: (Throwable?) -> Unit){
        userData.login(email,pw){exception ->
            userData.updateUserId()
            onResult(exception)
        }
    }

}