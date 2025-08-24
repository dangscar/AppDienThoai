package com.nlhd.domain.usecase.dashboard

import com.nlhd.domain.entity.dashboard.DashboardResponse
import com.nlhd.domain.repository.DashboardRepository
import com.nlhd.domain.resultWrapper.ResultWrapper

class GetDashboard(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(token: String): ResultWrapper<DashboardResponse> = repository.getDashboard(token)
}