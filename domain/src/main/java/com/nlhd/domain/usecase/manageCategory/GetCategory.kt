package com.nlhd.domain.usecase.manageCategory

import com.nlhd.domain.repository.ManageCategoryRepository

class GetCategory(
    private val repository: ManageCategoryRepository
) {
    suspend operator fun invoke(token: String) = repository.getCategory(token)
}