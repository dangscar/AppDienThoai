package com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info

data class User(
    val avatarUrl: String? = null,
    val email: String,
    val followersCount: String,
    val followingsCount: String,
    val id: Int,
    val name: String,
    val favoritesCount: String,
    val likesCount: String
)