package com.nlhd.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class ColorProduct(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val status: String,
    val value: String,
    val version: Version,
)