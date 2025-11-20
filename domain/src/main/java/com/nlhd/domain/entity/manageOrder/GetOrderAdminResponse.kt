package com.nlhd.domain.entity.manageOrder

data class GetOrderAdminResponse(
    val currentPage: Int,
    val orders: List<Order>,
)