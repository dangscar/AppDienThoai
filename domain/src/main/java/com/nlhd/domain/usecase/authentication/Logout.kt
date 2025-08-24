package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.repository.AuthenticationRepository

class Logout(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String) = repository.logout(token)
}