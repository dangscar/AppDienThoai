package com.nlhd.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nlhd.domain.usecase.order.OrderUseCase
import kotlinx.coroutines.flow.Flow

class OrderViewModel(
    private val orderUseCase: OrderUseCase
): ViewModel() {
    fun getOrders(token: String) = orderUseCase.getOrders(token).cachedIn(viewModelScope)
}
