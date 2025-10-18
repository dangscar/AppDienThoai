package com.nlhd.data.repository

import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.manageCategory.CategoryResponseDto
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.repository.ManageCategoryRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ManageCategoryRepositoryImp(
    private val ktor: HttpClient
): ManageCategoryRepository {
    override suspend fun getCategory(): ResultWrapper<CategoryResponse> {
        return try {
            val response = ktor.get(Utils.BASE_URL+"/api/category") {
                contentType(ContentType.Application.Json)
            }.body<CategoryResponseDto>()
            val responseDomain = response.toDomain(response)
            ResultWrapper.Success(responseDomain)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }

    }

}