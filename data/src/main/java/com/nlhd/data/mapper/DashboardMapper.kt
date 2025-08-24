package com.nlhd.data.mapper

import com.nlhd.data.model.dashboard.DashboardResponseDto
import com.nlhd.data.model.dashboard.Graph
import com.nlhd.domain.entity.dashboard.DashboardResponse

fun DashboardResponseDto.toDomain(dashboardResponseDto: DashboardResponseDto): DashboardResponse {
    return DashboardResponse(
        customerCount = dashboardResponseDto.customerCount,
        graph = dashboardResponseDto.graph.map { it.toDomain(it) },
        message = dashboardResponseDto.message,
        orderCount = dashboardResponseDto.orderCount,
        products = dashboardResponseDto.products,
        totalRevenue = dashboardResponseDto.totalRevenue
    )
}

fun Graph.toDomain(graph: Graph): com.nlhd.domain.entity.dashboard.Graph {
    return com.nlhd.domain.entity.dashboard.Graph(
        month = graph.month,
        revenue = graph.revenue,
        year = graph.year
    )
}