package com.nlhd.domain.usecase.product

import androidx.paging.PagingData
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProducts(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<PagingData<Product>> = repository.getProducts()

}