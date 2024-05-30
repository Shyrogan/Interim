package fr.umontpellier.interim.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

data class Application @JvmOverloads constructor(
    @DocumentId
    val id: String? = null,
    val offer: DocumentReference = Firebase.firestore.collection("offer")
        .document(""),
    val candidate: DocumentReference = Firebase.firestore.collection("user")
        .document(""),
    val cv: String? = "",
    val motivation_letter: String? = ""

)



