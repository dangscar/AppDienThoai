package com.nlhd.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.cart.AddCartResponse
import com.nlhd.domain.entity.productDetail.ProductDetailResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.product.ProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val productUseCase: ProductUseCase
): ViewModel() {

    private var _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    val state = _state.asStateFlow()

    private var _addCartState = MutableStateFlow<AddCartState>(AddCartState.Pending)
    val addCartState = _addCartState.asStateFlow()

    private var _version: MutableStateFlow<Int> = MutableStateFlow(0)
    val version = _version.asStateFlow()

    private var _color: MutableStateFlow<Int> = MutableStateFlow(0)
    val color = _color.asStateFlow()

    fun setAddCartState(state: AddCartState) {
        _addCartState.update { state }
    }

    fun setVersionAndColor(version: Int, color: Int) {
        _version.update { version }
        _color.update { color }
    }

    fun addCart(token: String, colorProductId: Int, quantity: Int, operator: Int) {
        _addCartState.update { AddCartState.Loading }
        viewModelScope.launch {
            productUseCase.addCart(token, colorProductId, quantity, operator).let { result ->
                when(result) {
                    is ResultWrapper.Failure -> {
                        _addCartState.update { AddCartState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _addCartState.update { AddCartState.Success(result.value as AddCartResponse) }
                    }
                }
            }
        }
    }

    fun getProductDetail(productId: Int, version: Int, color: Int) = viewModelScope.launch {
        productUseCase.getProductDetail(productId, version, color).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.update {
                        DetailState.Error(result.exception.message.toString())
                    }
                }
                is ResultWrapper.Success<*> -> {
                    _state.update {
                        DetailState.Success(result.value as ProductDetailResponse)
                    }
                }
            }
        }
    }
}

sealed class DetailState {
    data object Loading: DetailState()
    data class Success(val data: ProductDetailResponse): DetailState()
    data class Error(val message: String): DetailState()
}

sealed class AddCartState {
    object Pending: AddCartState()
    object Loading: AddCartState()
    data class Success(val data: AddCartResponse): AddCartState()
    data class Error(val message: String): AddCartState()
}