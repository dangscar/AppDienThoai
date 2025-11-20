package com.nlhd.data.model.manageOrder

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val created_at: String,
    val id: Int,
    val product: Product,
    val product_id: Int,
    val ram: Int,
    val storage: Int,
    val updated_at: String
)