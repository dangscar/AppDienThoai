package com.nlhd.domain.entity.login

import kotlinx.serialization.Serializable

data class LoginRequest(
    val email: String,
    val password: String
)