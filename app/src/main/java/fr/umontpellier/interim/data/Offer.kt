package fr.umontpellier.interim.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class Offer @JvmOverloads constructor(
    @DocumentId
    val id: String? = null,
    val name: String = "",
    val jobTitle: String = "",
    val profilesNeeded: List<String> = emptyList(),
    val remuneration: String = "",
    val description: String = "",
    val logo: String = "",
    val start: Timestamp = Timestamp.now(),
    val end: Timestamp = Timestamp.now(),
    val owner: DocumentReference? = null,
) {

    data class WithUser(val offer: Offer, val user: User)

}
