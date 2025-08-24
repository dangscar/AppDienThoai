package com.nlhd.domain.entity.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val battery: Int,
    val camera: String,
    val categoryId: Int,
    val cpu: String,
    val description: String,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double,
    val versions: List<Version>
)