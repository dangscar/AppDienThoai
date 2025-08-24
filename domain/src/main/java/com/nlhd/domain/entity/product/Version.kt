package com.nlhd.domain.entity.product

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val id: Int,
    val product_id: Int,
    val ram: Int,
    val storage: Int
)