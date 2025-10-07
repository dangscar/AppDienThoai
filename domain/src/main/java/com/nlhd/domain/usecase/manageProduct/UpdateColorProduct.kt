package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.manageProduct.UpdateColorProduct.UpdateColorRequest
import com.nlhd.domain.repository.ManageProductRepository

class UpdateColorProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, updateColorRequest: UpdateColorRequest) = repository.updateColorProduct(token, updateColorRequest)
}