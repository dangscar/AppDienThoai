package com.nlhd.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseDto(
    val current_page: Int,
    val last_page: Int,
    val message: String,
    val per_page: Int,
    val products: List<Product>,
    val total: Int
)