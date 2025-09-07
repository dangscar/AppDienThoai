package com.nlhd.domain.entity.manageProduct.LoadProduct


data class Product(
    val battery: Int,
    val camera: String,
    val category_id: Int,
    val colors: List<Color>,
    val cpu: String,
    val description: String? = null,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double,
    val versions: List<Version>
)