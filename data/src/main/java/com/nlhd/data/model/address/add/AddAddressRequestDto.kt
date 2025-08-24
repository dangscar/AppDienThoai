package com.nlhd.data.model.address.add

import kotlinx.serialization.Serializable

@Serializable
data class AddAddressRequestDto(
    val address: String,
    val description: String? = null,
    val name: String,
    val phone: String
)