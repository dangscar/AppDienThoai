package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.repository.ManageProductRepository

class GetColorProducts(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(token: String, versionProductId: Int) = repository.getColorProducts(token, versionProductId)
}