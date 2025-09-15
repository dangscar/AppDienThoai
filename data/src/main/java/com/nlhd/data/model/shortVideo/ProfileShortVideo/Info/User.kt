package com.nlhd.data.model.shortVideo.ProfileShortVideo.Info

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val avatar_url: String? = null,
    val email: String,
    val followers_count: Int,
    val followings_count: Int,
    val id: Int,
    val name: String,
    val received_favorites_count: Int,
    val received_likes_count: Int
)