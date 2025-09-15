package com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info

data class InfoProfileResponse(
    val caption: String,
    val createdAt: String,
    val favoritesCount: Int,
    val id: Int,
    val likesCount: Int,
    val productId: Int? = null,
    val shares: Int,
    val thumbnailUrl: String? = null,
    val user: User,
    val userId: Int,
    val videoUrl: String,
    val views: String
)