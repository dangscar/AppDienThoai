package com.nlhd.data.model.address.add

import com.nlhd.data.model.address.add.CustomerInfomationResponse
import kotlinx.serialization.Serializable

@Serializable
data class AddAddressResponseDto(
    val customerInfomation: CustomerInfomationResponse,
    val message: String
)