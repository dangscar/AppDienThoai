package com.nlhd.domain.entity.profile

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val address: String?,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String?,
    val role: String,
    val avatarUrl : String? = null
)