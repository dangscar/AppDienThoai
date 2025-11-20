package com.nlhd.domain.entity.manageOrder


data class OrderDetail(
    val color_product: ColorProduct,
    val color_product_id: Int,
    val created_at: String,
    val id: Int,
    val order_id: Int,
    val price: Int,
    val quantity: Int,
    val updated_at: String
)