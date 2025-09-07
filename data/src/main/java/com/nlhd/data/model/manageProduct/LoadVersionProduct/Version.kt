package com.nlhd.data.model.manageProduct.LoadVersionProduct

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val created_at: String,
    val id: Int,
    val product_id: Int,
    val ram: Int,
    val storage: Int,
    val updated_at: String
)