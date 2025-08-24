package com.nlhd.data.model.address.add

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInfomationResponse(
    val address: String,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val phoneNumber: String,
    val user_id: Int
)