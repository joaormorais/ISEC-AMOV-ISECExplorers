package com.example.amovtp.services

import android.util.Log
import com.example.amovtp.data.Category
import com.example.amovtp.data.LocalUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUserDataService {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }
    private var listenerUserData: ListenerRegistration? = null

    private var _userId: String = ""
    val userId: String
        get() = _userId

    fun createUserWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.exception == null)
                    _userId = auth.currentUser?.uid!!
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
            clearUserId()
        }
    }

    private fun clearUserId() {
        _userId = ""
    }

    fun startObserverGeoData(
        onFoundUser: (Map<String, Any>) -> Unit
    ) {
        stopObserver()
        listenerUserData =
            db.collection("UserData").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                for (doc in value!!) {
                    if (doc.data.getValue("userId") == _userId)
                        onFoundUser(doc.data)
                }
            }
    }

    private fun stopObserver() {
        listenerUserData?.remove()
    }

    fun addLocalUserToFirestore(newLocalUser: LocalUser, onResult: (Throwable?) -> Unit) {
        //val db = Firebase.firestore
        val savedVotesToCloud = hashMapOf(
            "userId" to newLocalUser.userId,
            "locationsApproved" to newLocalUser.locationsApproved,
            "pointsOfInterestApproved" to newLocalUser.pointsOfInterestApproved,
            "categoriesApproved" to newLocalUser.categoriesApproved,
            "pointsOfInterestClassified" to newLocalUser.pointsOfInterestClassified
        )
        db.collection("UserData").document(newLocalUser.userId).set(savedVotesToCloud)
            .addOnCompleteListener { result ->
                clearUserId()
                onResult(result.exception)
            }
    }

    fun updateLocalUserToFirestore(
        updatedLocalUser: LocalUser?,
        onResult: (Throwable?) -> Unit
    ) {
        //val db = Firebase.firestore
        val document = db.collection("UserData").document(updatedLocalUser?.userId.toString())

        db.runTransaction { transaction ->
            val doc = transaction.get(document)
            if (doc.exists()) {
                val data = doc.data
                if (data?.get("userId") != updatedLocalUser?.userId) {
                    transaction.update(document, "userId", updatedLocalUser?.userId)
                }
                if (data?.get("locationsApproved") != updatedLocalUser?.locationsApproved) {
                    transaction.update(document, "locationsApproved", updatedLocalUser?.locationsApproved)
                }
                if (data?.get("pointsOfInterestApproved") != updatedLocalUser?.pointsOfInterestApproved) {
                    transaction.update(document, "pointsOfInterestApproved", updatedLocalUser?.pointsOfInterestApproved)
                }
                if (data?.get("categoriesApproved") != updatedLocalUser?.categoriesApproved) {
                    transaction.update(document, "categoriesApproved", updatedLocalUser?.categoriesApproved)
                }
                if (data?.get("pointsOfInterestClassified") != updatedLocalUser?.pointsOfInterestClassified) {
                    transaction.update(document, "pointsOfInterestClassified", updatedLocalUser?.pointsOfInterestClassified)
                }
                null
            } else
                throw FirebaseFirestoreException(
                    "Doesn't exist",
                    FirebaseFirestoreException.Code.UNAVAILABLE
                )
        }.addOnCompleteListener { result ->
            onResult(result.exception)
        }
    }

}