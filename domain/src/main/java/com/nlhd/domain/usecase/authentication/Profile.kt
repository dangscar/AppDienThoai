package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.repository.AuthenticationRepository

class Profile(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String) = repository.profile(token)
}