package com.nlhd.domain.entity.manageProduct.LoadProduct

data class ProductsResponse(
    val message: String,
    val products: List<Product>
)