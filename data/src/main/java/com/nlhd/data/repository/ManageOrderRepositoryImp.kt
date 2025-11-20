package com.nlhd.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.data.remote.GetOrderAdminPagingSource
import com.nlhd.domain.entity.manageOrder.Order
import com.nlhd.domain.repository.ManageOrderRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class ManageOrderRepositoryImp(
    private val ktor: HttpClient
): ManageOrderRepository {
    override fun getOrderAdmin(token: String): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(pageSize = 12, prefetchDistance = 8),
            pagingSourceFactory = {
                GetOrderAdminPagingSource(
                    ktor = ktor,
                    token = token
                )
            },

        ).flow
    }
}