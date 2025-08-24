package com.nlhd.domain.entity.cart

data class CartResponse(
    val cartItems: List<CartItem> = emptyList(),
    val message: String,
    val totalCost: Int,
    val customerInformation: CustomerInformation? = null
)