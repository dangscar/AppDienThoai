package com.nlhd.data.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val message: String,
    val user: User ?= null
)