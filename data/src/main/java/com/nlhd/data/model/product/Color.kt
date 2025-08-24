package com.nlhd.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val id: Int,
    val image: String,
    val laravel_through_key: Int,
    val price: Int,
    val status: String
)