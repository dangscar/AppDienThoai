package com.nlhd.manage_product.VersionProductScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.UpdateVersionProduct.UpdateVersionProductRequest
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
    private var _state = MutableStateFlow<LoadVersionProductState>(LoadVersionProductState.Loading)
    val state = _state.asStateFlow()

    private var _ram = MutableStateFlow("")
    val ram = _ram.asStateFlow()
    private var _storage = MutableStateFlow("")
    val storage = _storage.asStateFlow()

    private var _updateVersionProductState = MutableStateFlow<UpdateVersionProductState>(UpdateVersionProductState.Idle)
    val updateVersionProductState = _updateVersionProductState.asStateFlow()
    private var _versionId = MutableStateFlow("")
    val versionId = _versionId.asStateFlow()

    fun onValueChangeStorage(value: String) = _storage.update { value }
    fun onValueChangeRam(value: String) = _ram.update { value }

    fun setVersionId(value: String) = _versionId.update { value }

    fun setUpdateVersionProductState(state: UpdateVersionProductState) = _updateVersionProductState.update { state }
    fun updateVersionProduct(token: String) = viewModelScope.launch {

        val updateVersionProductRequest = UpdateVersionProductRequest(
            ram = _ram.value,
            storage = _storage.value,
            versionProductId = _versionId.value
        )
        manageProductUseCase.updateVersionProduct(token, updateVersionProductRequest).let { result ->
            _updateVersionProductState.update { UpdateVersionProductState.Loading }
            when (result) {
                is ResultWrapper.Failure -> {
                    _updateVersionProductState.update { UpdateVersionProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _updateVersionProductState.update { UpdateVersionProductState.Success(result.value as MessageResponse) }
                }
            }
        }
    }
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

sealed class UpdateVersionProductState {
    object Idle: UpdateVersionProductState()
    object Loading: UpdateVersionProductState()
    data class Success(val messageResponse: MessageResponse): UpdateVersionProductState()
    data class Error(val message: String): UpdateVersionProductState()
}
