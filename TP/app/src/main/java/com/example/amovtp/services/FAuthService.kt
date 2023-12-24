package com.example.amovtp.services

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FAuthService {
    private val auth by lazy { Firebase.auth }

    private var _userId: String = ""
    val userId: String
        get() = _userId

    fun createUserWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }

    fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.exception == null) {
                    _userId = auth.currentUser?.uid!!
                    onResult(null)
                } else
                    onResult(result.exception)

            }
    }

    fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
            _userId = ""
        }
    }
}