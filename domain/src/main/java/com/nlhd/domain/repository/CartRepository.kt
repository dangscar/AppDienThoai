package com.nlhd.domain.repository

import com.nlhd.domain.entity.cart.AddCartResponse
import com.nlhd.domain.entity.cart.CartResponse
import com.nlhd.domain.entity.checkout.CheckoutRequest
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface CartRepository {
    suspend fun getCart(token: String): ResultWrapper<CartResponse>
    suspend fun addCart(token: String, colorProductId: Int, quantity: Int, operator: Int): ResultWrapper<AddCartResponse>
    suspend fun checkoutResponse(token: String, checkoutRequest: CheckoutRequest): ResultWrapper<CheckoutResponse>
}