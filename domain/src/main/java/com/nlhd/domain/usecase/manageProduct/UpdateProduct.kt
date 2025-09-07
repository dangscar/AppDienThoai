package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.manageProduct.UpdateProduct.UpdateProductRequest
import com.nlhd.domain.repository.ManageProductRepository

class UpdateProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, updateProductRequest: UpdateProductRequest) = repository.updateProduct(token, updateProductRequest)
}