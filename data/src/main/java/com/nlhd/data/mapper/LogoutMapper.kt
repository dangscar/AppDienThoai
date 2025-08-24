package com.nlhd.data.mapper

import com.nlhd.data.model.logout.LogoutResponseDto

fun LogoutResponseDto.toDomain(logoutResponseDto: LogoutResponseDto): com.nlhd.domain.entity.logout.LogoutResponse {
    return com.nlhd.domain.entity.logout.LogoutResponse(
        message = logoutResponseDto.message
    )
}