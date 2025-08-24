package com.nlhd.data.model.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val colors: List<Color>,
    val created_at: String,
    val deleted_at: String? = null,
    val id: Int,
    val product_id: Int,
    val ram: Int,
    val storage: Int,
    val updated_at: String
)