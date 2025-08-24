package com.nlhd.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponseDto(
    val customerCount: Int,
    val graph: List<Graph>,
    val message: String,
    val orderCount: Int,
    val products: Int,
    val totalRevenue: Int
)