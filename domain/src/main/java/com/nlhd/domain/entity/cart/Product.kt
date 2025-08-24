package com.nlhd.domain.entity.cart

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val battery: Int,
    val camera: String,
    val category_id: Int,
    val cpu: String,
    val description: String? = null,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double
)