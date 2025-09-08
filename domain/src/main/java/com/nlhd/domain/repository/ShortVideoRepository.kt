package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import kotlinx.coroutines.flow.Flow

interface ShortVideoRepository {
    fun getVideos(): Flow<PagingData<Video>>
}