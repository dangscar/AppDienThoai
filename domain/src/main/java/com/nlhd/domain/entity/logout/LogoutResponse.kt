package com.nlhd.domain.entity.logout

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val message: String
)