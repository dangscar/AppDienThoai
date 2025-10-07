package com.nlhd.data.mapper

import com.nlhd.data.model.signup.SignUpRequestDto
import com.nlhd.data.model.signup.SignUpResponseDto
import com.nlhd.domain.entity.signup.SignUpRequest
import com.nlhd.domain.entity.signup.SignUpResponse

fun SignUpRequestDto.toDomain(signUpRequestDto: SignUpRequestDto): SignUpRequest {
    return SignUpRequest(
        email = signUpRequestDto.email,
        name = signUpRequestDto.name,
        password = signUpRequestDto.password,
        passwordConfirmation = signUpRequestDto.password_confirmation
    )
}

fun SignUpResponseDto.toDomain(signUpResponseDto: SignUpResponseDto): SignUpResponse {
    return SignUpResponse(
        token = signUpResponseDto.token
    )
}