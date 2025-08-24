package com.nlhd.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.product.ProductResponseDto
import com.nlhd.domain.entity.product.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductPagingSource(
    private val ktor: HttpClient
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val page = params.key ?: 1
        return try {
            val responseDto = ktor.get("${Utils.BASE_URL}/api/?page=$page").body<ProductResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.products.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.products,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endOfPageReached) null else page + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}