package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val customer_info: Int,
    val selected_products: String
)