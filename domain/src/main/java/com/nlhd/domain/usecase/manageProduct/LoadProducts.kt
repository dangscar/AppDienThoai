package com.nlhd.domain.usecase.manageProduct

import androidx.paging.PagingData
import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import com.nlhd.domain.repository.ManageProductRepository
import kotlinx.coroutines.flow.Flow

class LoadProducts(
    private val repository: ManageProductRepository
) {
    operator fun invoke(token: String, search: String): Flow<PagingData<Product>> = repository.getProducts(token, search)
}