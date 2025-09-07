package com.nlhd.domain.entity.shortVideo.GetVideos

data class Video(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val channelId: String = "",
    val channelTitle: String = "",
    val categoryId: String = "",
    val linkMusic: String = "",
    val url: String = "",
    val isFullScreen: Boolean = false,
    val image: String? = null,
    var isLike: Boolean = false,
    var isFav: Boolean = false,
    var search: String = "Find related content",
    var images: List<String>? = null
)