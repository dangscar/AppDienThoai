package com.nlhd.domain.entity.manageProduct.LoadVersionProduct

data class Version(
    val id: Int,
    val productId: Int,
    val ram: Int,
    val storage: Int,
)