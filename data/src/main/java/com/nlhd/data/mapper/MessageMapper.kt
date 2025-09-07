package com.nlhd.data.mapper

import com.nlhd.data.model.Message.MessageResponseDto
import com.nlhd.domain.entity.Message.MessageResponse

fun MessageResponseDto.toDomain(messageResponseDto: MessageResponseDto): MessageResponse {
    return MessageResponse(
        message = message
    )
}