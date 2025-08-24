package com.nlhd.data.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartResponseDto(
    val cartItems: List<CartItem> = emptyList(),
    val message: String,
    val totalCost: Int = 0,
    val customerInformation: CustomerInformation? = null
)