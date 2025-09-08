package com.nlhd.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.repository.Client
import com.nlhd.data.summary.returnState
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import io.ktor.client.HttpClient

class GetVideosPagingSource(
    private val ktor: HttpClient,
    private val client: Client
): PagingSource<Int, Video>() {
    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return returnState(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 1
            val responseDto = client.videoResponseFriend(page)
            val response = responseDto.toDomain(responseDto)
            val endOfPageReached = response.results.isEmpty()
            if (!endOfPageReached) {
                LoadResult.Page(
                    data = response.results,
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