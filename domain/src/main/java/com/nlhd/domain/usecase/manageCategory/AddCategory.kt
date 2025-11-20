package com.nlhd.domain.usecase.manageCategory

import com.nlhd.domain.repository.ManageCategoryRepository

class AddCategory(
    private val repository: ManageCategoryRepository
) {
    suspend operator fun invoke(token: String, name: String) = repository.addCategory(token, name)
}