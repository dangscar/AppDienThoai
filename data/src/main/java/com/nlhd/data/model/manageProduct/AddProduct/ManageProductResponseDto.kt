package com.nlhd.data.model.manageProduct.AddProduct

import kotlinx.serialization.Serializable

@Serializable
data class ManageProductResponseDto(
    val color_product: ColorProduct,
    val message: String,
    val product: Product,
    val version_product: VersionProduct
)