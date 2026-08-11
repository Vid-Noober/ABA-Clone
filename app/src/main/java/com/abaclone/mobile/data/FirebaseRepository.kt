package com.abaclone.mobile.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object FirebaseRepository {

    fun registerUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val uid = RegistrationState.phone.hashCode().toString() + "_" + System.currentTimeMillis()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    val actualUid = firebaseUser?.uid ?: uid

                    val userDoc = hashMapOf(
                        "uid" to actualUid,
                        "phone" to RegistrationState.phone,
                        "email" to email,
                        "fullName" to RegistrationState.fullName,
                        "idNumber" to RegistrationState.idNumber,
                        "dateOfBirth" to RegistrationState.dateOfBirth,
                        "kycVerified" to RegistrationState.kycVerified,
                        "balance" to 1000.0,
                        "createdAt" to System.currentTimeMillis()
                    )

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(actualUid)
                        .set(userDoc)
                        .addOnSuccessListener {
                            // Upload ID photo if available
                            RegistrationState.idPhotoPath?.let { path ->
                                uploadIdPhoto(actualUid, path) { url ->
                                    Log.d("FirebaseRepository", "ID photo uploaded: $url")
                                }
                            }
                            RegistrationState.reset()
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseRepository", "Firestore write failed", e)
                            RegistrationState.reset()
                            onError(e.message ?: "Failed to save profile")
                        }
                } else {
                    task.exception?.message?.let { onError(it) } ?: run { onError("Registration failed") }
                }
            }
    }

    private fun uploadIdPhoto(
        uid: String,
        localPath: String,
        onResult: (String?) -> Unit
    ) {
        try {
            val storageRef = FirebaseStorage.getInstance().reference
                .child("users")
                .child(uid)
                .child("id_photo.jpg")

            val uri = Uri.fromFile(File(localPath))
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .update("idPhotoUrl", downloadUrl.toString())
                            .addOnFailureListener { e ->
                                Log.e("FirebaseRepository", "Failed to update idPhotoUrl in Firestore", e)
                            }
                        onResult(downloadUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseRepository", "ID photo upload failed", e)
                    onResult(null)
                }
        } catch (e: Exception) {
            Log.e("FirebaseRepository", "ID photo upload error", e)
            onResult(null)
        }
    }

    fun getCurrentUserBalance(onResult: (Double?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            onResult(null)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val balance = document.getDouble("balance")
                    onResult(balance)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Failed to get balance", e)
                onResult(null)
            }
    }

    fun getCurrentUserProfile(onResult: (String?, Double?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            onResult(null, null)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("fullName")
                    val balance = document.getDouble("balance")
                    onResult(name, balance)
                } else {
                    onResult(null, null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Failed to get user profile", e)
                onResult(null, null)
            }
    }

    fun fetchAndPopulateProfile(onDone: () -> Unit = {}) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            onDone()
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    RegistrationState.fullName = document.getString("fullName") ?: ""
                    RegistrationState.phone = document.getString("phone") ?: ""
                    RegistrationState.email = document.getString("email") ?: ""
                    RegistrationState.idNumber = document.getString("idNumber") ?: ""
                    RegistrationState.dateOfBirth = document.getString("dateOfBirth") ?: ""
                    RegistrationState.kycVerified = document.getBoolean("kycVerified") ?: false
                }
                onDone()
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Failed to fetch profile for RegistrationState", e)
                onDone()
            }
    }
}
