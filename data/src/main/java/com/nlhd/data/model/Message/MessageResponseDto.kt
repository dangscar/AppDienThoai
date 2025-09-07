package com.nlhd.data.model.Message

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDto(
    val message: String
)