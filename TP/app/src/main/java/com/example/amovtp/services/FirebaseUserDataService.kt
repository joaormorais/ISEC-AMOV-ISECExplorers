package com.example.amovtp.services

import com.example.amovtp.data.SavedVotes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUserDataService {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }
    //private var listenerUserData: ListenerRegistration? = null

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

    fun clearUserId(){
        _userId = ""
    }

    fun locateUserFirestore(
        onFoundUser: (Map<String, Any>) -> Unit
    ) {
        val db = Firebase.firestore
        val listenerUserData: ListenerRegistration?
        var foundUser = false
        listenerUserData =
            db.collection("UserData").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                for (doc in value!!)
                    if (doc.data.containsValue(_userId)) {
                        foundUser = true
                        onFoundUser(doc.data)
                        break
                    }
            }

        if (foundUser)
            listenerUserData.remove()
    }

    fun addSavedVotesToFirestore(newVotes: SavedVotes, onResult: (Throwable?) -> Unit) {
        //val db = Firebase.firestore

        val savedVotesToCloud = hashMapOf(
            "userId" to newVotes.userId,
            "locationsApproved" to newVotes.locationsApproved,
            "pointsOfInterestApproved" to newVotes.pointsOfInterestApproved,
            "categoriesApproved" to newVotes.categoriesApproved,
            "pointsOfInterestClassified" to newVotes.pointsOfInterestClassified
        )

        db.collection("UserData").document(newVotes.userId).set(savedVotesToCloud)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }

}