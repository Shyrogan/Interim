package fr.umontpellier.interim.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class Application @JvmOverloads constructor(
    @DocumentId
    val id: String? = null,
    val offer: DocumentReference? = null,
    val candidate: DocumentReference? = null,
    val cv: String? = "",
    val motivation_letter: String? = ""
)

data class WithUser(val application: Application, val user: User)



