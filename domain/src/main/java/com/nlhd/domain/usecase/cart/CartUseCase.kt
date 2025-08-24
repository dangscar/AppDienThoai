package com.nlhd.domain.usecase.cart

data class CartUseCase(
    val getCart: GetCart,
    val addCart: AddCart,
    val checkoutPreview: CheckoutPreview
)
