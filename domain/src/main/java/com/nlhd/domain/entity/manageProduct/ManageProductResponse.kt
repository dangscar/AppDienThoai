package com.nlhd.domain.entity.manageProduct

data class ManageProductResponse(
    val colorProduct: ColorProduct,
    val message: String,
    val product: Product,
    val versionProduct: VersionProduct
)