package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class SelectedProduct(
    val color: String,
    val color_product_id: String,
    val image: String,
    val name: String,
    val price: String,
    val quantity: String,
    val ram: String,
    val storage: String
)