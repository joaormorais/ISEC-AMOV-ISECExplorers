package com.example.amovtp.ui.viewmodels.usersViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UsersData

class LoginViewModelFactory(private val usersData: UsersData): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(usersData) as T
    }
}

class LoginViewModel(private val usersData: UsersData) : ViewModel() {



}