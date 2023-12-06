package com.example.amovtp.ui.viewmodels.usersViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amovtp.data.UsersData

class RegisterViewModelFactory(private val usersData: UsersData): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(usersData) as T
    }
}

class RegisterViewModel(private val usersData: UsersData) : ViewModel() {

}