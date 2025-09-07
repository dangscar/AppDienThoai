package com.nlhd.data.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDto(
    val address: String,
    val name: String,
    val phone: String
)