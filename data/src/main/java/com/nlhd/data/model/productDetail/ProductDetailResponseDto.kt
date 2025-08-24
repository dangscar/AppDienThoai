package com.nlhd.data.model.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponseDto(
    val message: String,
    val product: Product
)