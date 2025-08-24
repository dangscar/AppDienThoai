package com.nlhd.domain.usecase.checkout

import com.nlhd.domain.entity.checkout.CheckoutOrderRequest
import com.nlhd.domain.repository.CheckoutRepository

class CheckoutOrder(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(token: String, checkoutOrderRequest: CheckoutOrderRequest) = repository.checkoutOrder(token, checkoutOrderRequest)
}