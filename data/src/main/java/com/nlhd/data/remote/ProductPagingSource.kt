package com.nlhd.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.product.ProductResponseDto
import com.nlhd.data.summary.emptyPage
import com.nlhd.data.summary.returnPage
import com.nlhd.data.summary.returnState
import com.nlhd.data.summary.urlMethod
import com.nlhd.domain.entity.product.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductPagingSource(
    private val ktor: HttpClient
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return returnState(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val page = params.key ?: 1
        return try {
            val responseDto = ktor.get(urlMethod(page, "")).body<ProductResponseDto>()
            val response = responseDto.toDomain(responseDto)
            response.products.takeIf { it.isNotEmpty() } ?.let { returnPage(it, page) } ?: emptyPage()
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}