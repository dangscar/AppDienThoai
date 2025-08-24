package com.nlhd.domain.entity.cart

import kotlinx.serialization.Serializable

@Serializable
data class ColorProduct(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val product: Product,
    val version: Version,
    val version_product_id: Int
)