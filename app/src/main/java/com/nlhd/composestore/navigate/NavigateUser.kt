package com.nlhd.composestore.navigate

import kotlinx.serialization.Serializable

@Serializable
object Address

@Serializable
data class EditAddress(
    val id: Int
)

@Serializable
object Search

@Serializable
data class SearchSuccess(
    val search: String
)

@Serializable
object EditProfile

@Serializable
object SearchShortVideo

@Serializable
data class SearchShortSuccess(
    val search: String
)

@Serializable
data class ProfileShortVideo(
    val userId: Int
)

@Serializable
object UploadVideo

@Serializable
object UploadAvatar