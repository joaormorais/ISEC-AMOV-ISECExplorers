package com.example.amovtp.utils.fb

import android.content.res.AssetManager
import android.util.Log
import com.example.amovtp.data.Category
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.io.InputStream

class FStorageUtil {

    private var listenerRegistrationLocations: ListenerRegistration? = null
    private var listenerRegistrationPointOfInterest: ListenerRegistration? = null
    private var listenerRegistrationCategory: ListenerRegistration? = null

    fun startObserverGeoData(
        onNewLocations: (List<Map<String, Any>>) -> Unit,
        onNewPointsOfInterest: (List<Map<String, Any>>) -> Unit,
        onNewCategories: (List<Map<String, Any>>) -> Unit
    ) {
        stopObserver()
        val db = Firebase.firestore
        listenerRegistrationLocations =
            db.collection("GeoDataLocation").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyLocation = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyLocation.add(doc.data)
                }
                onNewLocations(everyLocation)
            }
        listenerRegistrationPointOfInterest =
            db.collection("GeoDataPointOfInterest").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyPointOfInterest = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyPointOfInterest.add(doc.data)
                }
                onNewPointsOfInterest(everyPointOfInterest)
            }
        listenerRegistrationCategory =
            db.collection("GeoDataCategory").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyCategory = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyCategory.add(doc.data)
                }
                onNewCategories(everyCategory)
            }
    }

    fun stopObserver() {
        listenerRegistrationLocations?.remove()
        listenerRegistrationPointOfInterest?.remove()
        listenerRegistrationCategory?.remove()
    }

    fun addLocationToFirestore(newLocation:Location,onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore

        val locationToCloud = hashMapOf(
            "userID" to newLocation.userId,
            "name" to newLocation.name,
            "description" to newLocation.description,
            "lat" to newLocation.lat,
            "long" to newLocation.long,
            "isManualCoords" to newLocation.isManualCoords,
            "pointsOfInterest" to newLocation.pointsOfInterest,
            "imgs" to newLocation.imgs,
            "votes" to newLocation.votes,
            "isApproved" to newLocation.isApproved,
        )

        db.collection("GeoDataLocation").document(newLocation.name).set(locationToCloud)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }

    fun addPointOfInterestToFirestore(newPointOfInterest: PointOfInterest,onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore

        val pointOfInterestToCloud = hashMapOf(
            "userID" to newPointOfInterest.userId,
            "name" to newPointOfInterest.name,
            "description" to newPointOfInterest.description,
            "lat" to newPointOfInterest.lat,
            "long" to newPointOfInterest.long,
            "isManualCoords" to newPointOfInterest.isManualCoords,
            "locations" to newPointOfInterest.locations,
            "category" to newPointOfInterest.category,
            "imgs" to newPointOfInterest.imgs,
            "classification" to newPointOfInterest.classification,
            "nClassifications" to newPointOfInterest.nClassifications,
            "votes" to newPointOfInterest.votes,
            "isApproved" to newPointOfInterest.isApproved
        )

        db.collection("GeoDataPointOfInterest").document(newPointOfInterest.name).set(pointOfInterestToCloud)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }

    fun addCategoryToFirestore(newCategory: Category,onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore

        val categoryToCloud = hashMapOf(
            "userID" to newCategory.userId,
            "name" to newCategory.name,
            "description" to newCategory.description,
            "img" to newCategory.img,
            "votes" to newCategory.votes,
            "isApproved" to newCategory.isApproved
        )

        db.collection("GeoDataCategory").document(newCategory.name).set(categoryToCloud)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }

    fun updateLocationDataInFirestore(onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        val v = db.collection("Scores").document("Level1")

        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i("Firestore", "updateDataInFirestore: Success? $exists")
                if (!exists) {
                    onResult(Exception("Doesn't exist"))
                    return@addOnSuccessListener
                }
                val value = it.getLong("nrgames") ?: 0
                v.update("nrgames", value + 1)
                onResult(null)
            }
            .addOnFailureListener { e ->
                onResult(e)
            }
    }

    fun updateDataInFirestoreTrans(onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        val v = db.collection("Scores").document("Level1")

        db.runTransaction { transaction ->
            val doc = transaction.get(v)
            if (doc.exists()) {
                val newnrgames = (doc.getLong("nrgames") ?: 0) + 1
                val newtopscore = (doc.getLong("topscore") ?: 0) + 100
                transaction.update(v, "nrgames", newnrgames)
                transaction.update(v, "topscore", newtopscore)
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

    fun removeDataFromFirestore(onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        val v = db.collection("Scores").document("Level1")

        v.delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

// Storage

    fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
        var istr: InputStream? = null
        try {
            istr = assetManager.open(strName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return istr
    }

//https://firebase.google.com/docs/storage/android/upload-files

    fun uploadFile(inputStream: InputStream, imgFile: String) {
        val storage = Firebase.storage
        val ref1 = storage.reference
        val ref2 = ref1.child("images")
        val ref3 = ref2.child(imgFile)

        val uploadTask = ref3.putStream(inputStream)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref3.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                println(downloadUri.toString())
                // something like:
                //   https://firebasestorage.googleapis.com/v0/b/p0405ansamov.appspot.com/o/images%2Fimage.png?alt=media&token=302c7119-c3a9-426d-b7b4-6ab5ac25fed9
            } else {
                // Handle failures
                // ...
            }
        }
    }

}