package com.nlhd.data.repository

import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.dashboard.DashboardResponseDto
import com.nlhd.data.model.login.LoginRequest
import com.nlhd.data.model.profile.ProfileResponseDto
import com.nlhd.domain.entity.dashboard.DashboardResponse
import com.nlhd.domain.repository.DashboardRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DashboardRepositoryImp(
    private val ktor: HttpClient
): DashboardRepository  {
    override suspend fun getDashboard(token: String): ResultWrapper<DashboardResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/dashboard") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<DashboardResponseDto>()

            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        } catch (e: Exception) {
            return ResultWrapper.Failure(e)
        }
    }
}