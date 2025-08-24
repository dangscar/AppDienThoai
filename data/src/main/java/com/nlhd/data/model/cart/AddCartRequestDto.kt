package com.nlhd.data.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class AddCartRequestDto(
    val color_product_id: Int,
    val `operator`: Int,
    val quantity: Int
)