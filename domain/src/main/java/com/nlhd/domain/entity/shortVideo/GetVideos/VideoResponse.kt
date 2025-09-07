package com.nlhd.domain.entity.shortVideo.GetVideos

data class VideoResponse(
    val status: Int = 0,
    val page: Int = 1,
    val totalPage: Int = 1,
    val results: List<Video> = emptyList()
)
