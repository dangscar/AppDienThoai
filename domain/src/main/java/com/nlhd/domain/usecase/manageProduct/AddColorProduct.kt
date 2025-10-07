package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.manageProduct.ColorProduct.AddColorProductRequest
import com.nlhd.domain.repository.ManageProductRepository

class AddColorProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, addColorProductRequest: AddColorProductRequest) = repository.addColorProducts(token, addColorProductRequest)
}