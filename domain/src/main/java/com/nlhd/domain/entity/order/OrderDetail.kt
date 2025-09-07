package com.nlhd.domain.entity.order

data class OrderDetail(
    val color_product: ColorProduct,
    val id: Int,
    val price: Int,
    val quantity: Int,
)