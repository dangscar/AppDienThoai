package com.nlhd.data.model.manageProduct.AddProduct

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val battery: String,
    val camera: String,
    val category_id: String,
    val cpu: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: String,
    val updated_at: String
)