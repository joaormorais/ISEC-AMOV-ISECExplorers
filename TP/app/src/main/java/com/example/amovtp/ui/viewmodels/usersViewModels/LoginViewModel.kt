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
            if(exception==null){
                userData.updateUserId()
            }
            onResult(exception)
        }
    }

    fun isLoginValid(
        email: String,
        password: String,
        emailNeeded: String,
        pwNeeded: String,
        errorMessage: (String) -> Unit
    ): Boolean {

        if (email.isBlank()){
            errorMessage(emailNeeded)
            return false
        }

        if (password.isBlank()){
            errorMessage(pwNeeded)
            return false
        }

        return true
    }

}