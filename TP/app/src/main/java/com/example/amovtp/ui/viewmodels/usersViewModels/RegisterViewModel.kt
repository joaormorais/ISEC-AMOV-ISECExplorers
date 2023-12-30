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

    fun isRegisterValid(
        email: String,
        password: String,
        confirmPassword: String,
        emailNeeded: String,
        pwNeeded: String,
        invalidEmailError: String,
        passwordsDontMatchError: String,
        errorMessage: (String) -> Unit
    ): Boolean {

        if (email.isBlank()){
            errorMessage(emailNeeded)
            return false
        }

        if(password.isBlank() || confirmPassword.isBlank()) {
            errorMessage(pwNeeded)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage(invalidEmailError)
            return false
        }

        if (password != confirmPassword) {
            errorMessage(passwordsDontMatchError)
            return false
        }

        return true
    }

}