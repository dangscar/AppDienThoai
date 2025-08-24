package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInformation(
    val address: String,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String,
    val userId: Int
)