package com.nlhd.domain.entity.shortVideo.Comments.GetComments

data class Comment(
    val content: String,
    val createdAt: String,
    val id: Int,
    val user: User,
    val userId: Int,
    val videoId: Int
)