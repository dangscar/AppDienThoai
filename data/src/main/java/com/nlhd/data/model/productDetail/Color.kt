package com.nlhd.data.model.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val created_at: String,
    val deleted_at: String? = null,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val updated_at: String,
    val value: String,
    val status: String,
    val version_product_id: Int
)