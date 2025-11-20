package com.nlhd.domain.entity.manageOrder

data class Order(
    val address: String,
    val created_at: String,
    val description: String,
    val fullName: String,
    val id: Int,
    val order_details: List<OrderDetail>,
    val phoneNumber: String,
    val status: String,
    val total_amount: Int,
    val updated_at: String,
    val user_id: Int
)