package com.nlhd.data.model.shortVideo.Comments.GetComments

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponseDto(
    val `data`: List<Data>,
)