package com.nlhd.domain.usecase.authentication

data class AuthenticationUseCase(
    val login: Login,
    val profile: Profile,
    val profileAdmin: ProfileAdmin,
    val logout: Logout
)
