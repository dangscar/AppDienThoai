package com.nlhd.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.productDetail.ProductDetailResponseDto
import com.nlhd.data.remote.ProductPagingSource
import com.nlhd.data.remote.SearchProductPagingSource
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.repository.ProductRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp(
    private val ktor: HttpClient
): ProductRepository  {
    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                ProductPagingSource(
                    ktor = ktor
                )
            }
        ).flow
    }

    override suspend fun getProductDetail(
        productId: Int,
        version: Int,
        color: Int,
    ): ResultWrapper<com.nlhd.domain.entity.productDetail.ProductDetailResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/product-detail/${productId}?version=${version}&color=${color}").body<ProductDetailResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override fun searchProducts(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                SearchProductPagingSource(
                    ktor = ktor,
                    query = query
                )
            }
        ).flow
    }


}