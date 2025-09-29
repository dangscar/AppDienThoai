package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.UpdateVersionProduct.UpdateVersionProductRequest
import com.nlhd.domain.repository.ManageProductRepository

class UpdateVersionProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, updateVersionProductRequest: UpdateVersionProductRequest) = repository.updateVersionProduct(token, updateVersionProductRequest)
}