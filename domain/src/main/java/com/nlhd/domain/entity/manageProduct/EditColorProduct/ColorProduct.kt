package com.nlhd.domain.entity.manageProduct.EditColorProduct

data class ColorProduct(
    val createdAt: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val status: String,
    val value: String,
    val versionId: Int
)