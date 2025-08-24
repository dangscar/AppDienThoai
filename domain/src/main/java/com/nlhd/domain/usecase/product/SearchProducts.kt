package com.nlhd.domain.usecase.product

import com.nlhd.domain.repository.ProductRepository

class SearchProducts(
    private val repository: ProductRepository
) {
    operator fun invoke(query: String) = repository.searchProducts(query)
}