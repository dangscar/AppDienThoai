package com.nlhd.data.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val cart_id: Int,
    val color_product: ColorProduct,
    val color_product_id: Int,
    val created_at: String,
    val id: Int,
    val price: Int,
    val quantity: Int,
    val updated_at: String
)