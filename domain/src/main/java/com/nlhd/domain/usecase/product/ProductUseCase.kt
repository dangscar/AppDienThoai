package com.nlhd.domain.usecase.product

import com.nlhd.domain.usecase.cart.AddCart

data class ProductUseCase(
    val getProducts: GetProducts,
    val getProductDetail: GetProductDetail,
    val addCart: AddCart,
    val searchProducts: SearchProducts
)