package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val customerInformation: CustomerInformation,
    val message: String,
    val selectedProducts: List<SelectedProduct>,
    val totalAmount: Int
)