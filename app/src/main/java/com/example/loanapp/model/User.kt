package com.example.loanapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (

    val username: String,
    val password: String?,
    val name: String,
    @SerialName("user_role")
    var userRole: String?,

)
