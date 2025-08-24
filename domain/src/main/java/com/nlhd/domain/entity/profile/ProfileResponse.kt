package com.nlhd.domain.entity.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val message: String,
    val user: User
)