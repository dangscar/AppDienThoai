package com.nlhd.domain.repository

import android.content.Context
import android.net.Uri
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.login.LoginResponse
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.entity.profile.UpdateProfileReponse
import com.nlhd.domain.entity.profile.UpdateProfileRequest
import com.nlhd.domain.entity.signup.SignUpRequest
import com.nlhd.domain.entity.signup.SignUpResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): ResultWrapper<LoginResponse>
    suspend fun profile(token: String): ResultWrapper<ProfileResponse>
    suspend fun profileAdmin(token: String): ResultWrapper<ProfileResponse>
    suspend fun logout(token: String): ResultWrapper<LogoutResponse>

    suspend fun updateProfile(token: String, updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileReponse>
    suspend fun uploadAvatar(token: String, context: Context, uri: Uri): ResultWrapper<MessageResponse>
    suspend fun signUp(signUpRequest: SignUpRequest): ResultWrapper<SignUpResponse>
}