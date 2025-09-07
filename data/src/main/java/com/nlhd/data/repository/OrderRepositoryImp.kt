package com.nlhd.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.data.remote.OrderPagingSource
import com.nlhd.domain.entity.order.Data
import com.nlhd.domain.repository.OrderRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImp(
    private val ktor: HttpClient
): OrderRepository {
    override fun getOrders(token: String): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { OrderPagingSource(ktor, token) }
        ).flow
    }
}