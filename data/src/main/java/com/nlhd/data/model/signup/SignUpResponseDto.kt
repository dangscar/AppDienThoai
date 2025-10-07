package com.nlhd.data.model.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponseDto(
    val token: String
)