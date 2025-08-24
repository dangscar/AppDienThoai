package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutOrderResponseDto(
    val message: String,
    val payment: Payment? = null
)