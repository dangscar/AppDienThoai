package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.repository.ManageProductRepository

class EditColorProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, id: Int) = repository.editColorProduct(token, id)
}