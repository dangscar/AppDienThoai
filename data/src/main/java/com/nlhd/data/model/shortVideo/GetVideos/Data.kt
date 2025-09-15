package com.nlhd.data.model.shortVideo.GetVideos

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val can_follow: Int,
    val caption: String,
    val comments_count: Int,
    val created_at: String? = null,
    val favorites_count: Int,
    val id: Int,
    val is_following: Int,
    val likes_count: Int,
    val product_id: Int? = null,
    val shares: Int,
    val thumbnail_url: String? = null,
    val updated_at: String,
    val user: User,
    val user_id: Int,
    val video_url: String,
    val views: Int,
    val is_liked: Int,
    val is_favorited: Int,
    val version_id: Int? = null,
    val color_id: Int? = null

)