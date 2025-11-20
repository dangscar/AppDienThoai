package com.nlhd.domain.repository

import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface ManageCategoryRepository {
    suspend fun getCategory(): ResultWrapper<CategoryResponse>
    suspend fun addCategory(token: String, name: String): ResultWrapper<MessageResponse>
}