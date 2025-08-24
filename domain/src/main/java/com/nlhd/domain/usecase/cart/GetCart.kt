package com.nlhd.domain.usecase.cart

import com.nlhd.domain.repository.CartRepository

class GetCart(
    private val repository: CartRepository
) {
    suspend operator fun invoke(token: String) = repository.getCart(token)

}