package com.nlhd.domain.usecase.product

import com.nlhd.domain.usecase.address.GetAddress
import com.nlhd.domain.usecase.cart.AddCart
import com.nlhd.domain.usecase.cart.CheckoutPreview

data class ProductUseCase(
    val getProducts: GetProducts,
    val getProductDetail: GetProductDetail,
    val addCart: AddCart,
    val searchProducts: SearchProducts,
)