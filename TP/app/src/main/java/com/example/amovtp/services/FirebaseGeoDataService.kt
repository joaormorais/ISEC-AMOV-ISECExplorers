package com.example.amovtp.services

import android.net.Uri
import android.util.Log
import com.example.amovtp.data.Category
import com.example.amovtp.data.Location
import com.example.amovtp.data.PointOfInterest
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.UUID

class FirebaseGeoDataService {

    private val db by lazy { Firebase.firestore }
    private var listenerRegistrationLocations: ListenerRegistration? = null
    private var listenerRegistrationPointOfInterest: ListenerRegistration? = null
    private var listenerRegistrationCategory: ListenerRegistration? = null

    fun startObserverGeoData(
        onNewLocations: (List<Map<String, Any>>) -> Unit,
        onNewPointsOfInterest: (List<Map<String, Any>>) -> Unit,
        onNewCategories: (List<Map<String, Any>>) -> Unit
    ) {
        stopObserver()
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
        val newId = UUID.randomUUID().toString()
        uploadImages("locations/$newId", newLocation.imgs) { paths ->
            if (paths.isNotEmpty()) {
                val locationToCloud = hashMapOf(
                    "id" to newId,
                    "userID" to newLocation.userId,
                    "name" to newLocation.name,
                    "description" to newLocation.description,
                    "lat" to newLocation.lat,
                    "long" to newLocation.long,
                    "isManualCoords" to newLocation.isManualCoords,
                    "pointsOfInterest" to newLocation.pointsOfInterest,
                    "imgs" to paths,
                    "votesForApproval" to newLocation.votesForApproval,
                    "isApproved" to newLocation.isApproved
                )
                db.collection("GeoDataLocations").document(newId).set(locationToCloud)
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
        val newId = UUID.randomUUID().toString()
        uploadImages(
            "pointsofinterest/$newId", newPointOfInterest.imgs
        ) { paths ->
            if (paths.isNotEmpty()) {
                val pointOfInterestToCloud = hashMapOf(
                    "id" to newId,
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
                db.collection("GeoDataPointsOfInterest").document(newId)
                    .set(pointOfInterestToCloud)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }
    }

    fun addCategoryToFirestore(newCategory: Category, onResult: (Throwable?) -> Unit) {
        val newId = UUID.randomUUID().toString()
        uploadImages("categories/$newId", listOf(newCategory.img)) { paths ->
            if (paths.isNotEmpty()) {
                val categoryToCloud = hashMapOf(
                    "id" to newId,
                    "userID" to newCategory.userId,
                    "name" to newCategory.name,
                    "description" to newCategory.description,
                    "img" to paths[0],
                    "votesForApproval" to newCategory.votesForApproval,
                    "isApproved" to newCategory.isApproved
                )
                db.collection("GeoDataCategories").document(newId).set(categoryToCloud)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }
    }

    fun updateLocationToFirestore(
        locationId: String,
        updatedLocation: Location?,
        onResult: (Throwable?) -> Unit
    ) {
        val document = db.collection("GeoDataLocations").document(locationId)
        db.runTransaction { transaction ->
            val doc = transaction.get(document)
            if (doc.exists()) {
                val data = doc.data
                if (data?.get("name") != updatedLocation?.name) {
                    transaction.update(document, "name", updatedLocation?.name)
                }
                if (data?.get("description") != updatedLocation?.description) {
                    transaction.update(document, "description", updatedLocation?.description)
                }
                if (data?.get("lat") != updatedLocation?.lat) {
                    transaction.update(document, "lat", updatedLocation?.lat)
                }
                if (data?.get("long") != updatedLocation?.long) {
                    transaction.update(document, "long", updatedLocation?.long)
                }
                if (data?.get("isManualCoords") != updatedLocation?.isManualCoords) {
                    transaction.update(document, "isManualCoords", updatedLocation?.isManualCoords)
                }
                if (data?.get("pointsOfInterest") != updatedLocation?.pointsOfInterest) {
                    transaction.update(
                        document,
                        "pointsOfInterest",
                        updatedLocation?.pointsOfInterest
                    )
                }
                if (data?.get("votesForApproval") != updatedLocation?.votesForApproval) {
                    transaction.update(
                        document,
                        "votesForApproval",
                        updatedLocation?.votesForApproval
                    )
                }
                if (data?.get("isApproved") != updatedLocation?.isApproved) {
                    transaction.update(document, "isApproved", updatedLocation?.isApproved)
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

    fun updatePointOfInterestToFirestore(
        pointOfInterestId: String,
        updatedPointOfInterest: PointOfInterest?,
        onResult: (Throwable?) -> Unit
    ) {
        val document = db.collection("GeoDataPointsOfInterest").document(pointOfInterestId)
        db.runTransaction { transaction ->
            val doc = transaction.get(document)
            if (doc.exists()) {
                val data = doc.data
                if (data?.get("name") != updatedPointOfInterest?.name) {
                    transaction.update(document, "name", updatedPointOfInterest?.name)
                }
                if (data?.get("description") != updatedPointOfInterest?.description) {
                    transaction.update(document, "description", updatedPointOfInterest?.description)
                }
                if (data?.get("lat") != updatedPointOfInterest?.lat) {
                    transaction.update(document, "lat", updatedPointOfInterest?.lat)
                }
                if (data?.get("long") != updatedPointOfInterest?.long) {
                    transaction.update(document, "long", updatedPointOfInterest?.long)
                }
                if (data?.get("isManualCoords") != updatedPointOfInterest?.isManualCoords) {
                    transaction.update(
                        document,
                        "isManualCoords",
                        updatedPointOfInterest?.isManualCoords
                    )
                }
                if (data?.get("locations") != updatedPointOfInterest?.locations) {
                    transaction.update(document, "locations", updatedPointOfInterest?.locations)
                }
                if (data?.get("category") != updatedPointOfInterest?.category) {
                    transaction.update(document, "category", updatedPointOfInterest?.category)
                }
                if (data?.get("classification") != updatedPointOfInterest?.classification) {
                    transaction.update(
                        document,
                        "classification",
                        updatedPointOfInterest?.classification
                    )
                }
                if (data?.get("nClassifications") != updatedPointOfInterest?.nClassifications) {
                    transaction.update(
                        document,
                        "nClassifications",
                        updatedPointOfInterest?.nClassifications
                    )
                }
                if (data?.get("votesForApproval") != updatedPointOfInterest?.votesForApproval) {
                    transaction.update(
                        document,
                        "votesForApproval",
                        updatedPointOfInterest?.votesForApproval
                    )
                }
                if (data?.get("isApproved") != updatedPointOfInterest?.isApproved) {
                    transaction.update(document, "isApproved", updatedPointOfInterest?.isApproved)
                }
                if (data?.get("votesForRemoval") != updatedPointOfInterest?.votesForRemoval) {
                    transaction.update(
                        document,
                        "votesForRemoval",
                        updatedPointOfInterest?.votesForRemoval
                    )
                }
                if (data?.get("isBeingRemoved") != updatedPointOfInterest?.isBeingRemoved) {
                    transaction.update(
                        document,
                        "isBeingRemoved",
                        updatedPointOfInterest?.isBeingRemoved
                    )
                }
                null
            } else
                throw FirebaseFirestoreException(
                    "Doesn't exist",
                    FirebaseFirestoreException.Code.UNAVAILABLE
                )
        }.addOnCompleteListener { result ->
            Log.d("FirebaseGeoDataService", "result.exception = " + result.exception)
            onResult(result.exception)
        }
    }

    fun updateCategoryToFirestore(
        categoryId: String,
        updatedCategory: Category?,
        onResult: (Throwable?) -> Unit
    ) {
        val document = db.collection("GeoDataCategories").document(categoryId)
        db.runTransaction { transaction ->
            val doc = transaction.get(document)
            if (doc.exists()) {
                val data = doc.data
                if (data?.get("name") != updatedCategory?.name) {
                    transaction.update(document, "name", updatedCategory?.name)
                }
                if (data?.get("description") != updatedCategory?.description) {
                    transaction.update(document, "description", updatedCategory?.description)
                }
                if (data?.get("votesForApproval") != updatedCategory?.votesForApproval) {
                    transaction.update(
                        document,
                        "votesForApproval",
                        updatedCategory?.votesForApproval
                    )
                }
                if (data?.get("isApproved") != updatedCategory?.isApproved) {
                    transaction.update(document, "isApproved", updatedCategory?.isApproved)
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

    //https://groups.google.com/g/firebase-talk/c/aG7GSR7kVtw
    fun deleteLocationFromFirestore(
        locationId: String,
        imagesPath: List<String>,
        onResult: (String) -> Unit
    ) {
        db.collection("GeoDataLocations").document(locationId).delete()
            .addOnSuccessListener { onResult("DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> onResult("Error deleting document: $e") }

        Firebase.storage.reference.child("locations/$locationId").listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach {
                it.delete()
                    .addOnSuccessListener { onResult("Image successfully deleted!") }
                    .addOnFailureListener { e -> onResult("Error deleting image: $e") }
            }
        }

    }

    fun deleteCategoryFromFirestore(
        categoryId: String,
        onResult: (String) -> Unit
    ) {
        db.collection("GeoDataCategories").document(categoryId).delete()
            .addOnSuccessListener { onResult("DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> onResult("Error deleting document: $e") }

        Firebase.storage.reference.child("categories/$categoryId/img1").delete()
            .addOnSuccessListener { onResult("Image successfully deleted!") }
            .addOnFailureListener { e -> onResult("Error deleting image: $e") }
    }

    fun uploadImages(
        folder: String,
        imgsToUpload: List<String>,
        onResult: (List<String>) -> Unit
    ) {
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
                if (returnPaths.size == imgsToUpload.size)
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
            fileReference.downloadUrl.addOnSuccessListener { uri ->
                returnPaths.add(uri.toString())
                if (returnPaths.size == imgsPathSearch.size)
                    onResult(returnPaths.toList())
            }.addOnFailureListener {
                onResult(emptyList())
            }
        }
    }

}