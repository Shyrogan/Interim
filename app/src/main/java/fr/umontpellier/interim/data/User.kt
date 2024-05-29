package fr.umontpellier.interim.data

data class User @JvmOverloads constructor(
    val first_name: String = "",
    val last_name: String = "",
    val nationality: String = "",
    val phone: String = "",
    val company: String = "",
    val company_position: String = "",
    val address: String = "",
    val files: List<String> = mutableListOf()
) {

    val isCandidate: Boolean
        get() = company.isEmpty()

    val isEmployer: Boolean
        get() = !isCandidate

}