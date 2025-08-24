package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.repository.AuthenticationRepository

class Login(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}