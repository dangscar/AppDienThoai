package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.repository.ManageProductRepository

class GetProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(id: Int, token: String) = repository.getProduct(id, token)
}