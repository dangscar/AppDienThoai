package com.nlhd.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val address: String,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val order_details: List<OrderDetail>,
    val phoneNumber: String,
    val status: String,
    val total_amount: Int,
)