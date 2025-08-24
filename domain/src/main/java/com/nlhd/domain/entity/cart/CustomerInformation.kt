package com.nlhd.domain.entity.cart

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInformation(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val address: String,
    val description: String? = null,
    val isSelected: Int,
    val user_id: Int,
    val deleted_at: String? = null,
    val created_at: String,
    val updated_at: String
)