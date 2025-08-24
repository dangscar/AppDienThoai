package com.nlhd.domain.usecase.product

import com.nlhd.domain.repository.ProductRepository

class GetProductDetail(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, version: Int, color: Int) = repository.getProductDetail(productId, version, color)

}