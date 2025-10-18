package com.nlhd.data.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    val categories: List<Category>,
    val message: String
)