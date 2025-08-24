package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInformation(
    val address: String,
    val created_at: String,
    val deleted_at: String? = null,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String,
    val updated_at: String,
    val user_id: Int
)