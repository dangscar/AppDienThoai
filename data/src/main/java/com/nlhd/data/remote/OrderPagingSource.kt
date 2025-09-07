package com.nlhd.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.order.OrderResponseDto
import com.nlhd.domain.entity.order.Data
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrderPagingSource(
    private val ktor: HttpClient,
    private val token: String
): PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val page = params.key ?: 1
        return try {

            val responseDto = ktor.get("${Utils.BASE_URL}/api/orders?page=$page"){
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<OrderResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.data.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.data,
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