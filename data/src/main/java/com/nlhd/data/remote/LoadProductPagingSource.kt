package com.nlhd.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.manageProduct.LoadProduct.ProductsResponseDto
import com.nlhd.data.summary.emptyPage
import com.nlhd.data.summary.returnPage
import com.nlhd.data.summary.returnState
import com.nlhd.data.summary.urlMethod
import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoadProductPagingSource(
    private val ktor: HttpClient,
    private val search: String,
    private val token: String
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return returnState(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/product?search=$search&page=$page") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<ProductsResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.products.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.products,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = page + 1
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