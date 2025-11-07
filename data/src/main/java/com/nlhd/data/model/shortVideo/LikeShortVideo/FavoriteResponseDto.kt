package com.nlhd.data.model.shortVideo.LikeShortVideo

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponseDto(
    val favorite_count: Int,
    val message: String
)