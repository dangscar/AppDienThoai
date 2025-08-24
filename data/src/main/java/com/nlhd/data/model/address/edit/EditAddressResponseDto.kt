package com.nlhd.data.model.address.edit

import kotlinx.serialization.Serializable

@Serializable
data class EditAddressResponseDto(
    val customerInformation: CustomerInformation,
    val message: String
)