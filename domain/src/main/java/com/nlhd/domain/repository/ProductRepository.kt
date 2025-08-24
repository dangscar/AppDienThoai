package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.entity.productDetail.ProductDetailResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<PagingData<Product>>
    suspend fun getProductDetail(productId: Int ,version: Int, color: Int): ResultWrapper<ProductDetailResponse>
    fun searchProducts(query: String): Flow<PagingData<Product>>
}
