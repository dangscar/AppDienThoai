package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponseDto(
    val customerInformation: CustomerInformation? = null,
    val message: String,
    val selectedProducts: List<SelectedProduct?>? = null,
    val total_amount: Int = 0
)