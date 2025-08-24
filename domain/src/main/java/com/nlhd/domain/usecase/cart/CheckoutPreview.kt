package com.nlhd.domain.usecase.cart

import com.nlhd.domain.entity.checkout.CheckoutRequest
import com.nlhd.domain.repository.CartRepository

class CheckoutPreview(
    private val repository: CartRepository
) {
    suspend operator fun invoke(token: String, checkoutRequest: CheckoutRequest) = repository.checkoutResponse(token, checkoutRequest)
}