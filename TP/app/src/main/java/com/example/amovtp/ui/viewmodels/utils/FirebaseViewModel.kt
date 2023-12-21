package com.example.amovtp.ui.viewmodels.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.amovtp.data.UserData
import com.example.amovtp.utils.fb.FAuthUtil
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

data class User(val name: String, val email: String, val picture: String?)

fun FirebaseUser.toUser(): User {
    val displayName = this.displayName ?: ""
    val strEmail = this.email ?: "n.d."
    val picture = this.photoUrl?.toString()
    return User(displayName, strEmail, picture)
}

class FirebaseViewModel() : ViewModel() {

    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val user: MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }

}