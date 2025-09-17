package com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info
data class InfoProfileResponse(
    val avatarUrl: String? = null,
    val canFollow: Boolean,
    val email: String,
    val followersCount: String,
    val followingsCount: String,
    val id: Int,
    val isFollowing: Boolean,
    val name: String,
    val favoritesCount: String,
    val likesCount: String,
    val videosCount: String
)