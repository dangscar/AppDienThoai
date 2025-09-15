package com.nlhd.domain.entity.shortVideo.GetVideos

data class Video(
    val canFollow: Boolean,
    val caption: String,
    val comments: String,
    val createdAt: String? = null,
    val favorites: String,
    val id: Int,
    val isFollowing: Boolean,
    val likes: String,
    val productId: Int? = null,
    val shares: String,
    val thumbnailUrl: String? = null,
    val user: User,
    val videoUrl: String,
    val views: String,
    val isLiked: Boolean,
    val isFavorited: Boolean,
    val versionId: Int? = null,
    val colorId: Int? = null
)