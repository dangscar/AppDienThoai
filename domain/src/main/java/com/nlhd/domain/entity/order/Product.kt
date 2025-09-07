package com.nlhd.domain.entity.order

data class Product(
    val battery: Int,
    val camera: String,
    val cpu: String,
    val description: String? = null,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double,
)