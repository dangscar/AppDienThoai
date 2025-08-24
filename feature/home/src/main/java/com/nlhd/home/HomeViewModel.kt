package com.nlhd.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.usecase.product.ProductUseCase
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    val productUseCase: ProductUseCase
): ViewModel() {

    private var _products: Flow<PagingData<Product>>? = null

    fun getProducts(): Flow<PagingData<Product>> {
        if (_products == null) {
            _products = productUseCase.getProducts().cachedIn(viewModelScope)
        }
        return _products!!
    }
}