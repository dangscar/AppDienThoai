package com.nlhd.data.model.manageProduct.LoadColorProduct

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val status: String,
    val updated_at: String,
    val value: String,
    val version_product_id: Int
)