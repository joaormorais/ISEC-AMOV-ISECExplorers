package com.example.amovtp.services

import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.amovtp.data.Category
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.utils.file.FileUtils
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class FirebaseGeoDataService {

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
            db.collection("GeoDataLocations").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyLocation = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyLocation.add(doc.data)
                }
                onNewLocations(everyLocation)
            }
        listenerRegistrationPointOfInterest =
            db.collection("GeoDataPointsOfInterest").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyPointOfInterest = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyPointOfInterest.add(doc.data)
                }
                onNewPointsOfInterest(everyPointOfInterest)
            }
        listenerRegistrationCategory =
            db.collection("GeoDataCategories").addSnapshotListener { value, e ->
                if (e != null)
                    return@addSnapshotListener
                val everyCategory = mutableListOf<Map<String, Any>>()
                for (doc in value!!) {
                    everyCategory.add(doc.data)
                }
                onNewCategories(everyCategory)
            }
    }

    private fun stopObserver() {
        listenerRegistrationLocations?.remove()
        listenerRegistrationPointOfInterest?.remove()
        listenerRegistrationCategory?.remove()
    }

    fun addLocationToFirestore(newLocation: Location, onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        uploadImages("locations/" + newLocation.name, newLocation.imgs){paths->
            if(paths.isNotEmpty()){
                val locationToCloud = hashMapOf(
                    "userID" to newLocation.userId,
                    "name" to newLocation.name,
                    "description" to newLocation.description,
                    "lat" to newLocation.lat,
                    "long" to newLocation.long,
                    "isManualCoords" to newLocation.isManualCoords,
                    "pointsOfInterest" to newLocation.pointsOfInterest,
                    "imgs" to paths,
                    "votesForApproval" to newLocation.votesForApproval,
                    "isApproved" to newLocation.isApproved,
                    "votesForRemoval" to newLocation.votesForRemoval,
                    "isBeingRemoved" to newLocation.isBeingRemoved
                )
                db.collection("GeoDataLocations").document(newLocation.name).set(locationToCloud)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }

        }
    }

    fun addPointOfInterestToFirestore(
        newPointOfInterest: PointOfInterest,
        onResult: (Throwable?) -> Unit
    ) {
        val db = Firebase.firestore
        uploadImages("pointsofinterest/" + newPointOfInterest.name, newPointOfInterest.imgs) { paths ->
            if (paths.isNotEmpty()) {
                val pointOfInterestToCloud = hashMapOf(
                    "userID" to newPointOfInterest.userId,
                    "name" to newPointOfInterest.name,
                    "description" to newPointOfInterest.description,
                    "lat" to newPointOfInterest.lat,
                    "long" to newPointOfInterest.long,
                    "isManualCoords" to newPointOfInterest.isManualCoords,
                    "locations" to newPointOfInterest.locations,
                    "category" to newPointOfInterest.category,
                    "imgs" to paths,
                    "classification" to newPointOfInterest.classification,
                    "nClassifications" to newPointOfInterest.nClassifications,
                    "votesForApproval" to newPointOfInterest.votesForApproval,
                    "isApproved" to newPointOfInterest.isApproved,
                    "votesForRemoval" to newPointOfInterest.votesForRemoval,
                    "isBeingRemoved" to newPointOfInterest.isBeingRemoved
                )
                db.collection("GeoDataPointsOfInterest").document(newPointOfInterest.name)
                    .set(pointOfInterestToCloud)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }
    }

    fun addCategoryToFirestore(newCategory: Category, onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        uploadImages("categories/" + newCategory.name, listOf(newCategory.img)) { paths ->
            if (paths.isNotEmpty()) {
                val categoryToCloud = hashMapOf(
                    "userID" to newCategory.userId,
                    "name" to newCategory.name,
                    "description" to newCategory.description,
                    "img" to paths,
                    "votesForApproval" to newCategory.votesForApproval,
                    "isApproved" to newCategory.isApproved,
                    "votesForRemoval" to newCategory.votesForRemoval,
                    "isBeingRemoved" to newCategory.isBeingRemoved
                )
                db.collection("GeoDataCategories").document(newCategory.name).set(categoryToCloud)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }
    }

    fun updateLocationToFirestore(
        currentLocationName: String,
        updatedLocation: Location,
        onResult: (Throwable?) -> Unit
    ) {
        val db = Firebase.firestore
        val document = db.collection("GeoDataLocations").document(currentLocationName)

        db.runTransaction { transaction ->
            val doc = transaction.get(document)
            if (doc.exists()) {
                Log.d("Firebase", "doc existe!")
                val data = doc.data
                if (data?.get("name") != updatedLocation.name) {
                    transaction.update(document, "name", updatedLocation.name)
                }
                if (data?.get("description") != updatedLocation.description) {
                    transaction.update(document, "description", updatedLocation.description)
                }
                if (data?.get("lat") != updatedLocation.lat) {
                    transaction.update(document, "lat", updatedLocation.lat)
                }
                if (data?.get("long") != updatedLocation.long) {
                    transaction.update(document, "long", updatedLocation.long)
                }
                if (data?.get("isManualCoords") != updatedLocation.isManualCoords) {
                    transaction.update(document, "isManualCoords", updatedLocation.isManualCoords)
                }
                if (data?.get("pointsOfInterest") != updatedLocation.pointsOfInterest) {
                    transaction.update(
                        document,
                        "pointsOfInterest",
                        updatedLocation.pointsOfInterest
                    )
                }
                if (data?.get("imgs") != updatedLocation.imgs) {
                    transaction.update(document, "imgs", updatedLocation.imgs)
                }
                if (data?.get("votesForApproval") != updatedLocation.votesForApproval) {
                    transaction.update(
                        document,
                        "votesForApproval",
                        updatedLocation.votesForApproval
                    )
                }
                if (data?.get("isApproved") != updatedLocation.isApproved) {
                    transaction.update(document, "isApproved", updatedLocation.isApproved)
                }
                if (data?.get("votesForRemoval") != updatedLocation.votesForRemoval) {
                    transaction.update(document, "votesForRemoval", updatedLocation.votesForRemoval)
                }
                if (data?.get("isBeingRemoved") != updatedLocation.isBeingRemoved) {
                    transaction.update(document, "isBeingRemoved", updatedLocation.isBeingRemoved)
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


    fun uploadImages(
        folder: String,
        imgsToUpload: List<String>,
        onResult: (List<String>) -> Unit
    ) { //https://firebase.google.com/docs/storage/android/upload-files
        val ref = Firebase.storage.reference.child(folder)
        val returnPaths: MutableList<String> = mutableListOf()

        imgsToUpload.forEachIndexed { index, img ->
            val file = Uri.fromFile(File(img))
            val imgPath = "img" + (index + 1)
            val imgUploadPath = ref.child(imgPath)
            val uploadTask = imgUploadPath.putFile(file)
            uploadTask.addOnFailureListener {
                onResult(emptyList())
            }.addOnSuccessListener {
                returnPaths.add(imgPath)
                if(returnPaths.size==imgsToUpload.size)
                    onResult(returnPaths)
            }
        }
    }

    fun downloadImages(
        folder: String,
        imgsPathSearch: List<String>,
        onResult: (List<String>) -> Unit
    ) {
        val ref = Firebase.storage.reference.child(folder)
        val returnPaths: MutableList<String> = mutableListOf()

        imgsPathSearch.forEachIndexed { index, img ->
            val fileReference = ref.child(img)
            Log.d("FirebaseGeoDataService", "fileReference = " + fileReference)
            fileReference.downloadUrl.addOnSuccessListener { uri ->
                returnPaths.add(uri.toString())
                if (returnPaths.size == imgsPathSearch.size)
                    onResult(returnPaths.toList())
            }.addOnFailureListener {
                onResult(emptyList())
            }
        }
    }

    /*fun removeDataFromFirestore(onResult: (Throwable?) -> Unit) {
        val db = Firebase.firestore
        val v = db.collection("Scores").document("Level1")

        v.delete()
            .addOnCompleteListener { onResult(it.exception) }
    }*/

}