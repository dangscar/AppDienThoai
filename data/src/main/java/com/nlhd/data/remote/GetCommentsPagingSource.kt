package com.nlhd.data.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.shortVideo.Comments.GetComments.CommentResponseDto
import com.nlhd.data.summary.returnState
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GetCommentsPagingSource(
    private val ktor: HttpClient,
    private val token: String,
    private val videoId: String
): PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return returnState(state)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val page = params.key ?: 1
        return try {
            val responseDto = ktor.get("${Utils.BASE_URL}/api/comments/${videoId}?page=$page"){
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<CommentResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.comments.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.comments,
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
            Log.d("AAA", e.message.toString())
            LoadResult.Error(e)
        }
    }
}