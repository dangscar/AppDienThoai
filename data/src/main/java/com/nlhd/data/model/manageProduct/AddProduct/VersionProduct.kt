package com.nlhd.data.model.manageProduct.AddProduct

import kotlinx.serialization.Serializable

@Serializable
data class VersionProduct(
    val created_at: String,
    val id: Int,
    val product_id: Int,
    val ram: String,
    val storage: String,
    val updated_at: String
)