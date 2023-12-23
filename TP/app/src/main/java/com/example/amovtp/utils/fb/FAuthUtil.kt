package com.example.amovtp.utils.fb

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FAuthUtil {
    private val auth by lazy { Firebase.auth }

    private var _userId: String? = null
    val userId: String?
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
                if (result.isSuccessful) {
                    onResult(null)
                    _userId = auth.currentUser?.uid
                } else
                    onResult(result.exception)

            }
    }

    fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
            _userId = null
        }
    }
}