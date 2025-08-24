package com.nlhd.data.model.address.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAddressRequestDto(
    val address: String,
    val description: String,
    val id: Int,
    val name: String,
    val phone: String
)