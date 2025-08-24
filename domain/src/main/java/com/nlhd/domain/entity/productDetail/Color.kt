package com.nlhd.domain.entity.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val value: String,
    val status: String,
    val versionId: Int
)