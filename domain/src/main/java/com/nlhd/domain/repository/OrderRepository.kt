package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.order.Data
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(token: String): Flow<PagingData<Data>>
}