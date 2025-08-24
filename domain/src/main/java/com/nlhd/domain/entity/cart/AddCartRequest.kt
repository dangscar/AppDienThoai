package com.nlhd.domain.entity.cart


data class AddCartRequest(
    val colorProductId: Int,
    val operator: Int,
    val quantity: Int
)