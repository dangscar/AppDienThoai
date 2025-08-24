package com.nlhd.domain.usecase.authentication

import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.repository.AuthenticationRepository
import com.nlhd.domain.resultWrapper.ResultWrapper

class ProfileAdmin(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String) : ResultWrapper<ProfileResponse> = repository.profileAdmin(token)

}