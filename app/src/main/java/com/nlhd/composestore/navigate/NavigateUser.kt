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