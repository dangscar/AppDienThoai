package com.nlhd.data.model.manageProduct.UpdateColorProduct

import kotlinx.serialization.Serializable

@Serializable
data class UpdateColorRequestDto(
    val color: String,
    val color_id: Int,
    val price: Int,
    val status: String,
    val value: String
)