package com.nlhd.data.model.shortVideo.GetVideos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponseDto(
    @SerialName("data")val data: List<Data>
)