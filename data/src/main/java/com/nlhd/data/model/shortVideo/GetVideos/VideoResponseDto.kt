package com.nlhd.data.model.shortVideo.GetVideos

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponseDto(
    val status: Int = 0,
    val page: Int = 1,
    val totalPage: Int = 1,
    val results: List<Video> = emptyList()
)
