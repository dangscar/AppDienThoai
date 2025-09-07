package com.nlhd.data.mapper

import com.nlhd.data.model.shortVideo.GetVideos.Video
import com.nlhd.data.model.shortVideo.GetVideos.VideoResponseDto
import com.nlhd.domain.entity.shortVideo.GetVideos.VideoResponse

fun VideoResponseDto.toDomain(videoResponseDto: VideoResponseDto): VideoResponse {
    return VideoResponse(
        status = videoResponseDto.status,
        page = videoResponseDto.page,
        totalPage = videoResponseDto.totalPage,
        results = videoResponseDto.results.map { it.toDomain(it) }
    )
}

fun Video.toDomain(video: Video): com.nlhd.domain.entity.shortVideo.GetVideos.Video {
    return com.nlhd.domain.entity.shortVideo.GetVideos.Video(
        id = video.id,
        title = video.title,
        description = video.description,
        publishedAt = video.publishedAt,
        channelId = video.channelId,
        channelTitle = video.channelTitle,
        categoryId = video.categoryId,
        linkMusic = video.linkMusic,
        url = video.url,
        isFullScreen = video.isFullScreen,
        image = video.image,
        isLike = video.isLike,
        isFav = video.isFav,
        search = video.search,
        images = video.images,

    )
}