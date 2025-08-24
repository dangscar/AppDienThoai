package com.nlhd.data.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class ColorProduct(
    val created_at: String,
    val deleted_at: String? = null,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val product: Product,
    val updated_at: String,
    val version: Version,
    val version_product_id: Int
)