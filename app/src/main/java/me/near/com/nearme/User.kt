package me.near.com.nearme

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(

    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val cellPhone: String = "",
    val currentCountry: String = "",
    val countryOfResidence: String = "",
    val jobTitle: String = ""
)

