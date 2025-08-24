package com.nlhd.domain.repository

import com.nlhd.domain.entity.checkout.CheckoutOrderRequest
import com.nlhd.domain.entity.checkout.CheckoutOrderResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface CheckoutRepository {
    suspend fun checkoutOrder(token: String, checkoutOrderRequest: CheckoutOrderRequest): ResultWrapper<CheckoutOrderResponse>
}