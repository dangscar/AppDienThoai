package com.nlhd.domain.entity.checkout


data class CheckoutOrderRequest(
    val customerInfoId: Int,
    val paymentMethod: String,
    val selectedProducts: String,
    val totalAmount: String
)