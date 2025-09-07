package com.nlhd.data.mapper

import com.nlhd.data.model.profile.ProfileResponseDto
import com.nlhd.data.model.profile.UpdateProfileRequestDto
import com.nlhd.data.model.profile.UpdateProfileResponseDto
import com.nlhd.data.model.profile.User
import com.nlhd.domain.entity.profile.UpdateProfileReponse
import com.nlhd.domain.entity.profile.UpdateProfileRequest

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

fun UpdateProfileRequestDto.toDomain(updateProfileRequestDto: UpdateProfileRequestDto): UpdateProfileRequest {
    return UpdateProfileRequest(
        address = updateProfileRequestDto.address,
        name = updateProfileRequestDto.name,
        phone = updateProfileRequestDto.phone
    )
}

fun UpdateProfileResponseDto.toDomain(updateProfileResponseDto: UpdateProfileResponseDto): UpdateProfileReponse {
    return UpdateProfileReponse(
        message = updateProfileResponseDto.message
    )
}