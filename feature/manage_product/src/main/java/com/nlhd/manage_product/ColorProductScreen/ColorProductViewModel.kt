package com.nlhd.manage_product.ColorProductScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageProduct.ColorProduct.AddColorProductRequest
import com.nlhd.domain.entity.manageProduct.LoadColorProduct.ColorResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ColorProductViewModel(
    private val manageProductUseCase: ManageProductUseCase
): ViewModel() {
    private var _state = MutableStateFlow<ColorProductState>(ColorProductState.Loading)
    val state = _state.asStateFlow()

    val statusList = listOf(
        "Còn hàng" to "in-stock",
        "Hết hàng" to "out-stock"
    )

    private var _manageColorAction = MutableStateFlow(ColorProductAction(
        price = "18000000",
        image = null,
        status = Pair(first = statusList.first().first, second = statusList.first().second),
        color = "Black",
        valueColor = "#000000"
    ))
    val manageColorAction = _manageColorAction.asStateFlow()

    private var _addColorProductState = MutableStateFlow<AddColorProductState>(AddColorProductState.Idle)
    val addColorProductState = _addColorProductState.asStateFlow()

    private var _isExpand: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpand: StateFlow<Boolean> = _isExpand.asStateFlow()

    private var _isValidColor: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isValidColor: StateFlow<Boolean> = _isValidColor.asStateFlow()

    fun setPrice(price: String) {
        _manageColorAction.update { it.copy(price = price) }
    }

    fun uploadImage(uri: Uri) {
        _manageColorAction.update { it.copy(image = uri) }
    }

    fun setValidColor(isValidColor: Boolean) {
        _isValidColor.update { isValidColor }
    }

    fun setExpand(isExpand: Boolean) { _isExpand.update { isExpand } }

    fun setColorValue(valueColor: String) {
        _manageColorAction.update { it.copy(valueColor = valueColor) }
    }

    fun setColor(color: String) {
        _manageColorAction.update { it.copy(color = color) }
    }

    fun setStatus(status: Pair<String, String>) {
        _manageColorAction.update {
            it.copy(status = status)
        }
        _isExpand.update { false }
    }

    fun addColorProduct(token: String, versionId: Int, context: Context) = viewModelScope.launch {
        val addColorProductRequest = AddColorProductRequest(
            context = context,
            versionId = versionId,
            price = _manageColorAction.value.price,
            status = _manageColorAction.value.status.second,
            image = _manageColorAction.value.image!!,
            value = _manageColorAction.value.valueColor,
            color = _manageColorAction.value.color
        )
        manageProductUseCase.addColorProducts(token, addColorProductRequest).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _addColorProductState.update { AddColorProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _addColorProductState.update { AddColorProductState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun getColorProducts(token: String, versionProductId: Int) = viewModelScope.launch {
        manageProductUseCase.getColorProducts(token, versionProductId).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.update { ColorProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _state.update { ColorProductState.Success(result.value as ColorResponse) }
                }
            }
        }
    }
}

sealed class ColorProductState {
    object Loading : ColorProductState()
    data class Success(val data: ColorResponse) : ColorProductState()
    data class Error(val message: String) : ColorProductState()
}

sealed class AddColorProductState {
    object Idle: AddColorProductState()
    object Loading : AddColorProductState()
    data class Success(val data: MessageResponse) : AddColorProductState()
    data class Error(val message: String) : AddColorProductState()
}
data class ColorProductAction(
    val price: String,
    val image: Uri?,
    val status: Pair<String, String>,
    val color: String,
    val valueColor: String
)