package com.nlhd.data.model.manageOrder

import kotlinx.serialization.Serializable

@Serializable
data class ColorProduct(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val status: String,
    val updated_at: String,
    val value: String,
    val version: Version,
    val version_product_id: Int
)