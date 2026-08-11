package com.abaclone.mobile.data

/**
 * Simple in-memory holder for data collected across the onboarding flow.
 * Fields are populated screen-by-screen before being committed to Firestore
 * inside FirebaseRepository.registerUser() once the password is set.
 */
object RegistrationState {
    var phone: String = ""
    var email: String = ""
    var fullName: String = ""
    var idNumber: String = ""
    var dateOfBirth: String = ""
    var kycVerified: Boolean = false
    var idPhotoPath: String? = null

    fun reset() {
        phone = ""
        email = ""
        fullName = ""
        idNumber = ""
        dateOfBirth = ""
        kycVerified = false
        idPhotoPath = null
    }
}
