package com.nlhd.domain.entity.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(
    val message: String,
    val product: Product
)