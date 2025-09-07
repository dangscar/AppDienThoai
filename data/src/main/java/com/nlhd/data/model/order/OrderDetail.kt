package com.nlhd.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderDetail(
    val color_product: ColorProduct,
    val id: Int,
    val price: Int,
    val quantity: Int,
)