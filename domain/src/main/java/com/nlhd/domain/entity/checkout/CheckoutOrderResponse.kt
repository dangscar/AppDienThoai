package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutOrderResponse(
    val message: String,
    val payment: Payment
)