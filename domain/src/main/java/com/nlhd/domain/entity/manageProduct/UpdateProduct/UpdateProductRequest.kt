package com.nlhd.domain.entity.manageProduct.UpdateProduct

data class UpdateProductRequest(
    val battery: Int,
    val camera: String,
    val categoryId: Int,
    val cpu: String,
    val description: String,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double
)