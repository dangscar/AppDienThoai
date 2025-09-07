package com.nlhd.data.model.manageProduct.EditProduct

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val battery: Int,
    val camera: String,
    val category_id: Int,
    val cpu: String,
    val description: String,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double
)