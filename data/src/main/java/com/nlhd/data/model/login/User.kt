package com.nlhd.data.model.login

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val address: String?,
    val created_at: String?,
    val email: String,
    val email_verified_at: String?,
    val id: Int,
    val name: String,
    val phone: String?,
    val role: String,
    val updated_at: String?
)