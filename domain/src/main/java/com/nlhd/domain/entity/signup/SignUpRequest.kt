package com.nlhd.domain.entity.signup

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String,
    val passwordConfirmation: String
)