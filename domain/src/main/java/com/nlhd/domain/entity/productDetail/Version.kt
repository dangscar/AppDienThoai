package com.nlhd.domain.entity.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val colors: List<Color>,
    val id: Int,
    val productId: Int,
    val ram: Int,
    val storage: Int,
)