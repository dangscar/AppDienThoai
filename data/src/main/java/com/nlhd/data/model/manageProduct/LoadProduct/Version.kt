package com.nlhd.data.model.manageProduct.LoadProduct

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val id: Int,
    val product_id: Int,
    val ram: Int,
    val storage: Int
)