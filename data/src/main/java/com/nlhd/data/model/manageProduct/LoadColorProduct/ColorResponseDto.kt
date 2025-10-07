package com.nlhd.data.model.manageProduct.LoadColorProduct

import kotlinx.serialization.Serializable

@Serializable
data class ColorResponseDto(
    val colors: List<Color>,
    val message: String
)