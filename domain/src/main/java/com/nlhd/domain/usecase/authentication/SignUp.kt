package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.entity.signup.SignUpRequest
import com.nlhd.domain.repository.AuthenticationRepository

class SignUp(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(signUpRequest: SignUpRequest) = repository.signUp(signUpRequest)
}