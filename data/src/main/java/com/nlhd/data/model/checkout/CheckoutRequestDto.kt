package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequestDto(
    val customer_info: Int,
    val selected_products: String
)