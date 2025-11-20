package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.manageOrder.Order
import kotlinx.coroutines.flow.Flow

interface ManageOrderRepository {
    fun getOrderAdmin(token: String): Flow<PagingData<Order>>
}