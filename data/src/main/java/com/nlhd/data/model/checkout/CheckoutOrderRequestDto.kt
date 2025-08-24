package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutOrderRequestDto(
    val customer_information_id: Int,
    val payment_method: String,
    val selected_products: String,
    val total_amount: String
)