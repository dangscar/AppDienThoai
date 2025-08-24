package com.nlhd.domain.entity.login

import kotlinx.serialization.Serializable

data class User(
    val address: String?,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String?,
    val role: String
)