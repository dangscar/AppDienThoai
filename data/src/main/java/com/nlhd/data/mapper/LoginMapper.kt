package com.nlhd.data.mapper

import com.nlhd.data.model.login.LoginRequest
import com.nlhd.data.model.login.LoginResponse
import com.nlhd.data.model.login.User

fun LoginRequest.toDomain(loginRequest: LoginRequest): com.nlhd.domain.entity.login.LoginRequest {
    return com.nlhd.domain.entity.login.LoginRequest(
        email = loginRequest.email,
        password = loginRequest.password
    )
}

fun LoginResponse.toDomain(loginResponse: LoginResponse): com.nlhd.domain.entity.login.LoginResponse {
    return com.nlhd.domain.entity.login.LoginResponse(
        token = loginResponse.token,
        user = loginResponse.user.toDomain(loginResponse.user)
    )
}

fun User.toDomain(user: User): com.nlhd.domain.entity.login.User {
    return com.nlhd.domain.entity.login.User(
        address = user.address,
        email = user.email,
        id = user.id,
        name = user.name,
        phone = user.phone,
        role = user.role
    )
}