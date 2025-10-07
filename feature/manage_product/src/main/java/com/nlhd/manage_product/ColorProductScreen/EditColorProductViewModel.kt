package com.nlhd.manage_product.ColorProductScreen

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageProduct.EditColorProduct.EditColorResponse
import com.nlhd.domain.entity.manageProduct.UpdateColorProduct.UpdateColorRequest
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditColorProductViewModel(
    private val manageProductUseCase: ManageProductUseCase
): ViewModel() {

    private var _state = MutableStateFlow<EditColorProductState>(EditColorProductState.Loading)
    val state = _state.asStateFlow()

    private var _updateProductState = MutableStateFlow<UpdateColorProductState>(UpdateColorProductState.Pending)
    val updateProductState = _updateProductState.asStateFlow()

    val statusList = listOf(
        "Còn hàng" to "in-stock",
        "Hết hàng" to "out-stock"
    )

    private var _manageColorAction = MutableStateFlow(ColorProductAction(
        price = "0",
        image = null,
        status = Pair(first = statusList.first().first, second = statusList.first().second),
        color = "Black",
        valueColor = "#000000"
    ))
    val manageColorAction = _manageColorAction.asStateFlow()

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

    fun updateColorProduct(token: String, context: Context, colorId: Int) = viewModelScope.launch {
        _updateProductState.update { UpdateColorProductState.Loading }
        val updateColorRequest = UpdateColorRequest(
            context = context,
            colorId = colorId,
            color = manageColorAction.value.color,
            price = manageColorAction.value.price.toInt(),
            image = manageColorAction.value.image,
            status = manageColorAction.value.status.second,
            value = manageColorAction.value.valueColor
        )
        manageProductUseCase.updateColorProduct(token, updateColorRequest).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _updateProductState.update { UpdateColorProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _updateProductState.update { UpdateColorProductState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun editColorProduct(token: String, id: Int) = viewModelScope.launch {
        manageProductUseCase.editColorProduct(token, id).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.update { EditColorProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    val colorProduct = (result.value as EditColorResponse).colorProduct
                    val statusFirst = if (colorProduct.status == "in-stock") statusList.first().first else statusList.last().first

                    setPrice(colorProduct.price.toString())
                    setStatus(Pair(first = statusFirst, second = colorProduct.status))
                    setColor(colorProduct.name)
                    setColorValue(colorProduct.value)
                    uploadImage(colorProduct.image.toUri())

                    _state.update { EditColorProductState.Success(result.value as EditColorResponse) }
                }
            }
        }

    }
}

sealed class EditColorProductState {
    object Loading: EditColorProductState()
    data class Success(val data: EditColorResponse): EditColorProductState()
    data class Error(val message: String): EditColorProductState()
}

sealed class UpdateColorProductState {
    object Pending: UpdateColorProductState()
    object Loading: UpdateColorProductState()
    data class Success(val data: MessageResponse): UpdateColorProductState()
    data class Error(val message: String): UpdateColorProductState()
}