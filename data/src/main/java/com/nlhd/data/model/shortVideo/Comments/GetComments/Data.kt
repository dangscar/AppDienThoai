package com.nlhd.data.model.shortVideo.Comments.GetComments

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val content: String,
    val created_at: String,
    val id: Int,
    val updated_at: String,
    val user: User,
    val user_id: Int,
    val video_id: Int
)