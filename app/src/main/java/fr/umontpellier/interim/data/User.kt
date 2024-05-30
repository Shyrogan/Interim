package fr.umontpellier.interim.data

import com.google.firebase.firestore.DocumentId

data class User @JvmOverloads constructor(
    @DocumentId
    val id: String? = null,
    val first_name: String = "",
    val last_name: String = "",
    val nationality: String = "",
    val phone: String = "",
    val company: String = "",
    val company_position: String = "",
    val address: String = "",
    val files: List<String> = mutableListOf(),
    val cv: String = ""
) {

    val isCandidate: Boolean
        get() = company.isEmpty()

    val isEmployer: Boolean
        get() = !isCandidate

}

