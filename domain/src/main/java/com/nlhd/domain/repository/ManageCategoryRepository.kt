package com.nlhd.domain.repository

import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface ManageCategoryRepository {
    suspend fun getCategory(): ResultWrapper<CategoryResponse>
}