package com.nlhd.domain.entity.cart

data class CartItem(
    val cart_id: Int,
    val color_product: ColorProduct,
    val color_product_id: Int,
    val id: Int,
    val price: Int,
    val quantity: Int
)