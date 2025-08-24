package com.nlhd.data.model.address.get

import kotlinx.serialization.Serializable

@Serializable
data class AddressResponseDto(
    val customerInfomations: List<CustomerInfomation>,
    val message: String
)