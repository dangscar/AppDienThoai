package com.nlhd.manage_product.LoadVersionProductScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.manageProduct.LoadVersionProduct.LoadVersionProductResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadVersionProductViewModel(
    private val manageProductUseCase: ManageProductUseCase,
): ViewModel() {
    private val _state = MutableStateFlow<LoadVersionProductState>(LoadVersionProductState.Loading)
    val state = _state.asStateFlow()

    fun getVersionProducts(token: String, productId: Int) = viewModelScope.launch {
        manageProductUseCase.getVersionProducts(token, productId).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.update {   LoadVersionProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _state.update { LoadVersionProductState.Success(result.value as LoadVersionProductResponse) }
                }
            }
        }
    }
}

sealed class LoadVersionProductState {
    object Loading: LoadVersionProductState()
    data class Success(val data: LoadVersionProductResponse): LoadVersionProductState()
    data class Error(val message: String): LoadVersionProductState()
}

