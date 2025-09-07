package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.entity.profile.UpdateProfileRequest
import com.nlhd.domain.repository.AuthenticationRepository

class UpdateProfile(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String, updateProfileRequest: UpdateProfileRequest) = repository.updateProfile(token, updateProfileRequest)
}