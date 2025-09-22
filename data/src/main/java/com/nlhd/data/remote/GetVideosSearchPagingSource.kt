package com.nlhd.data.remote

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.shortVideo.GetVideos.VideoResponseDto
import com.nlhd.data.summary.returnState
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GetVideosSearchPagingSource(
    private val ktor: HttpClient,
    private val token: String,
    private val search: String
): PagingSource<Int, Video>() {
    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return returnState(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 1
            val responseDto = ktor.get(Utils.BASE_URL+"/api/video?q=${Uri.encode(search)}&page=$page") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<VideoResponseDto>()
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.videos.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.videos,
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