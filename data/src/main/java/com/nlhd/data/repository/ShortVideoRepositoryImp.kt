package com.nlhd.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.data.remote.GetVideosPagingSource
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.repository.ShortVideoRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class ShortVideoRepositoryImp(
    private val ktor: HttpClient,
    private val client: Client
): ShortVideoRepository {
    override fun getVideos(): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                GetVideosPagingSource(
                    ktor = ktor,
                    client = client
                )
            }

        ).flow
    }
}