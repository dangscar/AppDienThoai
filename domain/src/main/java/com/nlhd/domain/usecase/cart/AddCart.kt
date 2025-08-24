package com.nlhd.domain.usecase.cart

import com.nlhd.domain.repository.CartRepository

class AddCart(
    private val repository: CartRepository
) {
    suspend operator fun invoke(token: String, colorProductId: Int, quantity: Int, operator: Int) = repository.addCart(token, colorProductId, quantity, operator)
}