package com.nlhd.data.model.address.get

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInfomation(
    val address: String,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String,
    val user_id: Int
)