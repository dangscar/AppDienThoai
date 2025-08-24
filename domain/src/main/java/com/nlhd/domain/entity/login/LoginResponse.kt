package com.nlhd.domain.entity.login

import kotlinx.serialization.Serializable

data class LoginResponse(
    val token: String,
    val user: User
)