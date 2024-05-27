package fr.umontpellier.interim.data

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

data class Offer @JvmOverloads constructor(
    val name: String = "",
    val description: String = "",
    val logo: String = "",
    val start: Timestamp = Timestamp.now(),
    val end: Timestamp = Timestamp.now(),
    val owner: DocumentReference = Firebase.firestore.collection("user")
        .document(Firebase.auth.currentUser?.uid ?: ""),
)
