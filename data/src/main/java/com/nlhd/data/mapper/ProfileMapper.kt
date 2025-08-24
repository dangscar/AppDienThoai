package com.nlhd.data.mapper

import com.nlhd.data.model.profile.ProfileResponseDto
import com.nlhd.data.model.profile.User

fun ProfileResponseDto.toDomain(profileResponseDto: ProfileResponseDto): com.nlhd.domain.entity.profile.ProfileResponse {
    return com.nlhd.domain.entity.profile.ProfileResponse(
        message = profileResponseDto.message,
        user = profileResponseDto.user!!.toDomain(profileResponseDto.user)
    )
}

fun User.toDomain(user: User): com.nlhd.domain.entity.profile.User {
    return com.nlhd.domain.entity.profile.User(
        address = user.address,
        email = user.email,
        id = user.id,
        name = user.name,
        phone = user.phone,
        role = user.role
    )
}