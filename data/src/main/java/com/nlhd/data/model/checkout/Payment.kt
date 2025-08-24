package com.nlhd.data.model.checkout

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val amount_paid: String,
    val created_at: String,
    val id: Int,
    val order_id: Int,
    val payment_method: String,
    val status: String,
    val updated_at: String
)