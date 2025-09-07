package com.nlhd.data.model.manageProduct.LoadProduct

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val id: Int,
    val image: String,
    val price: Int
)