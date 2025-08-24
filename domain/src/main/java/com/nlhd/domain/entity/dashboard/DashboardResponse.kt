package com.nlhd.domain.entity.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val customerCount: Int,
    val graph: List<Graph>,
    val message: String,
    val orderCount: Int,
    val products: Int,
    val totalRevenue: Int
)