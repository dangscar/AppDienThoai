package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class ProductCheckout(
    val color_product_id: String,
    val name: String,
    val image: String,
    val price: String,
    val quantity: String,
    val color: String,
    val ram: String,
    val storage: String
)