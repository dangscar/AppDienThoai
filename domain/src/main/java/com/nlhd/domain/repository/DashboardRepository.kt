package com.nlhd.domain.repository

import com.nlhd.domain.entity.dashboard.DashboardResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface DashboardRepository {
    suspend fun getDashboard(token: String): ResultWrapper<DashboardResponse>
}