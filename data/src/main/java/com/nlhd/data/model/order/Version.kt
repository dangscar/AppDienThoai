package com.nlhd.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val id: Int,
    val product: Product,
    val ram: Int,
    val storage: Int,
)