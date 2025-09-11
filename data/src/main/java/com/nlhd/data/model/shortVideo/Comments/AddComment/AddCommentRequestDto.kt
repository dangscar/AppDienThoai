package com.nlhd.data.model.shortVideo.Comments.AddComment

import kotlinx.serialization.Serializable

@Serializable
data class AddCommentRequestDto(
    val content: String,
    val video_id: Int
)