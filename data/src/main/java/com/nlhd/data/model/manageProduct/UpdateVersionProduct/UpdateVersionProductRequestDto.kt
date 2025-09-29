package com.nlhd.data.model.manageProduct.UpdateVersionProduct

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVersionProductRequestDto(
    val ram: String,
    val storage: String,
    val version_product_id: String
)