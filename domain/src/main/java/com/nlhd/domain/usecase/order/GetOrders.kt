package com.nlhd.domain.usecase.order

import com.nlhd.domain.repository.OrderRepository

class GetOrders(
    private val repository: OrderRepository
) {
    operator fun invoke(token: String) = repository.getOrders(token)

}