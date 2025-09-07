package com.nlhd.manage_product.AddVersionProductScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageProduct.UploadVersionProduct.UploadVersionProduct
import com.nlhd.domain.repository.ManageProductRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddVersionProductViewModel(
    private val manageProductRepository: ManageProductRepository
): ViewModel() {

    val statusList = listOf(
        "Còn hàng" to "in-stock",
        "Hết hàng" to "out-stock"
    )

    private var _isValidColor: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isValidColor: StateFlow<Boolean> = _isValidColor.asStateFlow()
    private var _isExpand: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpand: StateFlow<Boolean> = _isExpand.asStateFlow()

    private var _addVersionAction: MutableStateFlow<AddVersionAction> = MutableStateFlow(AddVersionAction(
        price = "4000000",
        image = null,
        status = Pair(first = statusList.first().first, second = statusList.first().second),
        color = "Black",
        valueColor = "#000000",
        ram = "6",
        storage = "256",
        productId = 1
    ))
    val addVersionAction: StateFlow<AddVersionAction> = _addVersionAction.asStateFlow()

    private var _state = MutableStateFlow<AddVersionProductState>(AddVersionProductState.Pending)
    val state: StateFlow<AddVersionProductState> = _state.asStateFlow()

    fun setPrice(price: String) = _addVersionAction.update { it.copy(price = price) }
    fun setStatus(status: Pair<String, String>) {
        _addVersionAction.update { it.copy(status = status) }
        _isExpand.update { false }
    }
    fun setColor(color: String) = _addVersionAction.update { it.copy(color = color) }
    fun setColorValue(valueColor: String) = _addVersionAction.update { it.copy(valueColor = valueColor) }
    fun setRam(ram: String) = _addVersionAction.update { it.copy(ram = ram) }
    fun setStorage(storage: String) = _addVersionAction.update { it.copy(storage = storage) }
    fun setProductId(productId: Int) = _addVersionAction.update { it.copy(productId = productId) }
    fun setImage(image: Uri?) = _addVersionAction.update { it.copy(image = image) }

    fun setValidColor(isValidColor: Boolean) = _isValidColor.update { isValidColor }
    fun setExpand(isExpand: Boolean) = _isExpand.update { isExpand }

    fun addVersionProduct(context: Context, token: String) = viewModelScope.launch {
        val uploadVersionProduct = UploadVersionProduct(
            price = addVersionAction.value.price,
            status = addVersionAction.value.status.second,
            color = addVersionAction.value.color,
            value = addVersionAction.value.valueColor,
            ram = addVersionAction.value.ram,
            storage = addVersionAction.value.storage,
            productId = addVersionAction.value.productId,
            image = addVersionAction.value.image!!,
            context = context
        )
        manageProductRepository.addVersionProduct(uploadVersionProduct, token).let { result ->
            when (result) {
                is ResultWrapper.Failure -> { _state.update { AddVersionProductState.Error(result.exception.message.toString()) } }
                is ResultWrapper.Success<*> -> {
                    _state.update { AddVersionProductState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

}

sealed class AddVersionProductState {
    object Pending: AddVersionProductState()
    object Loading: AddVersionProductState()
    data class Success(val data: MessageResponse): AddVersionProductState()
    data class Error(val message: String): AddVersionProductState()
}

data class AddVersionAction(
    val price: String,
    val image: Uri?,
    val status: Pair<String, String>,
    val color: String,
    val valueColor: String,
    val ram: String,
    val storage: String,
    val productId: Int
)