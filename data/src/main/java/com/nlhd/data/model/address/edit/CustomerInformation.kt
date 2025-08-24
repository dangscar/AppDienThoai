package com.nlhd.data.model.address.edit

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInformation(
    val address: String,
    val description: String,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String
)