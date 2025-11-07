package com.nlhd.data.model.shortVideo.LikeShortVideo

import kotlinx.serialization.Serializable

@Serializable
data class LikeResponseDto(
    val like_count: Int,
    val message: String
)