package com.nlhd.data.model.productDetail

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val battery: Int,
    val camera: String,
    val category_id: Int,
    val cpu: String,
    val created_at: String,
    val deleted_at: String? = null,
    val description: String,
    val id: Int,
    val name: String,
    val os: String,
    val screenSize: Double,
    val updated_at: String,
    val versions: List<Version>
)