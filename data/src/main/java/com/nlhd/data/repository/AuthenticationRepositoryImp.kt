package com.nlhd.data.repository

import android.util.Log
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.login.LoginRequest
import com.nlhd.data.model.logout.LogoutResponseDto
import com.nlhd.data.model.profile.ProfileResponseDto
import com.nlhd.data.model.profile.UpdateProfileRequestDto
import com.nlhd.data.model.profile.UpdateProfileResponseDto
import com.nlhd.domain.entity.login.LoginResponse
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.entity.profile.UpdateProfileReponse
import com.nlhd.domain.entity.profile.UpdateProfileRequest
import com.nlhd.domain.repository.AuthenticationRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthenticationRepositoryImp(
    private val ktor: HttpClient
): AuthenticationRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<LoginResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email = email, password = password))
            }.body<com.nlhd.data.model.login.LoginResponse>()

            val response = responseDto.toDomain(loginResponse = responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)

        }
    }

    override suspend fun profile(token: String): ResultWrapper<ProfileResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/user") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<ProfileResponseDto>()

            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        } catch (e: Exception) {
            return ResultWrapper.Failure(e)
        }

    }

    override suspend fun profileAdmin(token: String): ResultWrapper<ProfileResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/admin") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<ProfileResponseDto>()

            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        } catch (e: Exception) {
            return ResultWrapper.Failure(e)
        }
    }

    override suspend fun logout(token: String): ResultWrapper<LogoutResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/logout") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<LogoutResponseDto>()
            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        }catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun updateProfile(
        token: String,
        updateProfileRequest: UpdateProfileRequest
    ): ResultWrapper<UpdateProfileReponse> {
        return try {
            val responseDto = ktor.put(Utils.BASE_URL+"/api/profile") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProfileRequestDto(
                        address = updateProfileRequest.address,
                        name = updateProfileRequest.name,
                        phone = updateProfileRequest.phone
                    )
                )
            }.body<UpdateProfileResponseDto>()
            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }

    }


}