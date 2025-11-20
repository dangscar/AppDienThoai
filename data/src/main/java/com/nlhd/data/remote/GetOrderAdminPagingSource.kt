package com.nlhd.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.manageOrder.GetOrderAdminResponseDto
import com.nlhd.data.model.product.ProductResponseDto
import com.nlhd.data.model.shortVideo.Comments.GetComments.CommentResponseDto
import com.nlhd.data.summary.returnState
import com.nlhd.domain.entity.manageOrder.Order
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GetOrderAdminPagingSource(
    private val ktor: HttpClient,
    private val token: String
): PagingSource<Int, Order>() {
    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return returnState(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 1
        return try {
            val responseDto = ktor.get("${Utils.BASE_URL}/api/orders?page=$page"){
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<GetOrderAdminResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.orders.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.orders,
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