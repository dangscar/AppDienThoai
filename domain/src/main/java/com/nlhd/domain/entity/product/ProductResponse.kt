package com.nlhd.domain.entity.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val message: String,
    val products: List<Product>,
)