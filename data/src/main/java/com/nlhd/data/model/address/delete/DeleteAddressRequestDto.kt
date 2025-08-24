package com.nlhd.data.model.address.delete

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAddressRequestDto(
    val id: Int
)