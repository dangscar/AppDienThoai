package com.nlhd.data.model.shortVideo.Comments.GetComments

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val avatar_url: String? = null,
    val id: Int,
    val name: String
)