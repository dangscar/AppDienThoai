package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.repository.ManageProductRepository

class GetVersionProducts(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, productId: Int) = repository.getVersionProducts(token, productId)
}