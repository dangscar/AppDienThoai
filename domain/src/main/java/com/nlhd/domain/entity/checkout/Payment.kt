package com.nlhd.domain.entity.checkout

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val amountPaid: String,
    val createdAt: String,
    val id: Int,
    val orderId: Int,
    val paymentMethod: String,
    val status: String,
    val updatedAt: String
)