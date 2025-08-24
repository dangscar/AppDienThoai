package com.nlhd.domain.repository

import com.nlhd.domain.entity.login.LoginResponse
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): ResultWrapper<LoginResponse>
    suspend fun profile(token: String): ResultWrapper<ProfileResponse>
    suspend fun profileAdmin(token: String): ResultWrapper<ProfileResponse>
    suspend fun logout(token: String): ResultWrapper<LogoutResponse>
}