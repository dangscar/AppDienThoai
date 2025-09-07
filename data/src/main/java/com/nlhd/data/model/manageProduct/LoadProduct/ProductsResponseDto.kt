package com.nlhd.data.model.manageProduct.LoadProduct

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(
    val message: String,
    val products: List<Product>
)