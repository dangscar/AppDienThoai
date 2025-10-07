package com.nlhd.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.Message.MessageResponseDto
import com.nlhd.data.model.login.LoginRequest
import com.nlhd.data.model.logout.LogoutResponseDto
import com.nlhd.data.model.profile.ProfileResponseDto
import com.nlhd.data.model.profile.UpdateProfileRequestDto
import com.nlhd.data.model.profile.UpdateProfileResponseDto
import com.nlhd.data.model.signup.SignUpRequestDto
import com.nlhd.data.model.signup.SignUpResponseDto
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.login.LoginResponse
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.entity.profile.UpdateProfileReponse
import com.nlhd.domain.entity.profile.UpdateProfileRequest
import com.nlhd.domain.entity.signup.SignUpRequest
import com.nlhd.domain.entity.signup.SignUpResponse
import com.nlhd.domain.repository.AuthenticationRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import org.json.JSONObject

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

    override suspend fun uploadAvatar(
        token: String,
        context: Context,
        uri: Uri
    ): ResultWrapper<MessageResponse> {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return ResultWrapper.Failure(Exception("Không thể mở tệp hình ảnh"))
            val byteArray = inputStream.readBytes()
            inputStream.close()

            val headers = headersOf(
                HttpHeaders.ContentDisposition to listOf("form-data; name=\"image\"; filename=\"upload.jpg\""),
                HttpHeaders.ContentType to listOf("image/jpeg")
            )

            val multipartData = MultiPartFormDataContent(
                formData {
                    append("image", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
                    })
                }
            )

            val responseDto = ktor.post(Utils.BASE_URL+"/api/image") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(multipartData)
            }.body<MessageResponseDto>()

            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)

        } catch (e: Exception) {
            return ResultWrapper.Failure(e)
        }


    }

    override suspend fun signUp(signUpRequest: SignUpRequest): ResultWrapper<SignUpResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    SignUpRequestDto(
                        email = signUpRequest.email,
                        name = signUpRequest.name,
                        password = signUpRequest.password,
                        password_confirmation = signUpRequest.passwordConfirmation
                    )
                )
            }.body<SignUpResponseDto>()

            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)

        }
    }


}