package com.nlhd.domain.usecase.manageOrder

import com.nlhd.domain.repository.ManageOrderRepository

class GetOrdersAdmin(
    private val repository: ManageOrderRepository
) {
    operator fun invoke(token: String) = repository.getOrderAdmin(token)
}