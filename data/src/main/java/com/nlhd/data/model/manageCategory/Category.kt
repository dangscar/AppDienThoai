package com.nlhd.data.model.manageCategory

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val created_at: String?,
    val deleted_at: String?,
    val id: Int,
    val name: String,
    val updated_at: String?
)