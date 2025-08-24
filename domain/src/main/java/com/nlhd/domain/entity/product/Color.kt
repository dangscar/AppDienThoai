package com.nlhd.domain.entity.product

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val id: Int,
    val image: String,
    val price: Int,
    val status: String
)