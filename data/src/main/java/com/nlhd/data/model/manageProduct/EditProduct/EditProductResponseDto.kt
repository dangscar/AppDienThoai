package com.nlhd.data.model.manageProduct.EditProduct

import kotlinx.serialization.Serializable

@Serializable
data class EditProductResponseDto(
    val message: String,
    val product: Product
)