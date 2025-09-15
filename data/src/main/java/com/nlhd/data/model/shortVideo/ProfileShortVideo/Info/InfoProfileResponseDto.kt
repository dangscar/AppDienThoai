package com.nlhd.data.model.shortVideo.ProfileShortVideo.Info

import kotlinx.serialization.Serializable

@Serializable
data class InfoProfileResponseDto(
    val caption: String,
    val created_at: String,
    val favorites_count: Int,
    val id: Int,
    val likes_count: Int,
    val product_id: Int? = null,
    val shares: Int,
    val thumbnail_url: String? = null,
    val user: User,
    val user_id: Int,
    val video_url: String,
    val views: Int
)