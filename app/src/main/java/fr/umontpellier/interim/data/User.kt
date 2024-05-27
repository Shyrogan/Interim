package fr.umontpellier.interim.data

data class User @JvmOverloads constructor(
    val first_name: String = "",
    val last_name: String = "",
    val nationality: String = "",
    val phone: String = "",
    val type: String = "",
)
