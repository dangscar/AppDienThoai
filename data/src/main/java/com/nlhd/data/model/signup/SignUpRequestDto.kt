package com.nlhd.data.model.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val email: String,
    val name: String,
    val password: String,
    val password_confirmation: String
)