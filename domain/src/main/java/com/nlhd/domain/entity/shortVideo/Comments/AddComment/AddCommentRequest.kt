package com.nlhd.domain.entity.shortVideo.Comments.AddComment

data class AddCommentRequest(
    val content: String,
    val videoId: Int
)