package com.nlhd.domain.entity.manageOrder


data class ColorProduct(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val status: String,
    val updated_at: String,
    val value: String,
    val version: Version,
    val version_product_id: Int
)