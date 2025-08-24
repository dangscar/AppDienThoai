package com.nlhd.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.address.delete.DeleteAddressResponse
import com.nlhd.domain.entity.address.get.AddressResponse
import com.nlhd.domain.entity.address.get.SelectedAddressResponse
import com.nlhd.domain.entity.cart.AddCartResponse
import com.nlhd.domain.entity.cart.CartResponse
import com.nlhd.domain.entity.checkout.CheckoutRequest
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.address.AddressUseCase
import com.nlhd.domain.usecase.cart.CartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartUseCase: CartUseCase,
    private val addressUseCase: AddressUseCase
): ViewModel() {

    private var _state = MutableStateFlow<CartState>(CartState.Loading)
    val state = _state.asStateFlow()

    private var _addCartState = MutableStateFlow<AddCartState>(AddCartState.Pending)
    val addCartState = _addCartState.asStateFlow()

    private var _totalPrice = MutableStateFlow<Int>(0)
    val totalPrice = _totalPrice.asStateFlow()

    private var _stateCheckoutPreview = MutableStateFlow<CheckoutPreviewState>(CheckoutPreviewState.Pending)
    val stateCheckoutPreview = _stateCheckoutPreview.asStateFlow()

    private var _isSelected = MutableStateFlow<List<Boolean>>(listOf())
    val isSelected = _isSelected.asStateFlow()

    private var _isOpenSheet = MutableStateFlow<Boolean>(false)
    val isOpenSheet = _isOpenSheet.asStateFlow()

    private var _stateAddress = MutableStateFlow<AddressState>(AddressState.Pending)
    val stateAddress = _stateAddress.asStateFlow()

    private var _stateSelectedAddress = MutableStateFlow<SelectedAddressState>(SelectedAddressState.Pending)
    val stateSelectedAddress = _stateSelectedAddress.asStateFlow()

    private var _stateDeleteAddress = MutableStateFlow<DeleteAddressState>(DeleteAddressState.Pending)
    val stateDeleteAddress = _stateDeleteAddress.asStateFlow()

    fun deleteAddress(token: String, id: Int) = viewModelScope.launch {
        _stateDeleteAddress.update { DeleteAddressState.Loading }
        addressUseCase.deleteAddress(token, id).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateDeleteAddress.update { DeleteAddressState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateDeleteAddress.update { DeleteAddressState.Success(result.value as DeleteAddressResponse) }
                }
            }
        }
    }

    fun updateStateSuccessAddress() {
        _stateSelectedAddress.update { SelectedAddressState.Pending }
        _stateDeleteAddress.update { DeleteAddressState.Pending }
    }

    fun selectedAddress(token: String, id: Int) = viewModelScope.launch {
        _stateSelectedAddress.update { SelectedAddressState.Loading }
        addressUseCase.selectedAddress(token, id).let { result ->
            when(result) {
                is ResultWrapper.Failure -> {
                    _stateSelectedAddress.update { SelectedAddressState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateSelectedAddress.update { SelectedAddressState.Success(result.value as SelectedAddressResponse) }
                }
            }
        }
    }

    fun getAddress(token: String) = viewModelScope.launch {
        _stateAddress.update { AddressState.Loading }
        addressUseCase.getAddress(token).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateAddress.update { AddressState.Error(result.exception.message.toString()) }
                }

                is ResultWrapper.Success<*> -> {
                    _stateAddress.update { AddressState.Success(result.value as AddressResponse) }
                }
            }
        }
    }

    fun setOpenSheet(value: Boolean){
        _isOpenSheet.update { value }
    }

    fun setSelected(index: Int, value: Boolean) {
        val list = _isSelected.value.toMutableList()
        list[index] = value
        _isSelected.update { list }
    }

    fun setAddCartState(state: AddCartState) {
        _addCartState.update { state }
    }

    fun setCheckoutPreviewState(state: CheckoutPreviewState) {
        _stateCheckoutPreview.update { state }
    }

    fun setTotalPrice(price: Int) {
        _totalPrice.update { price }
    }

    fun addCart(token: String, colorProductId: Int, quantity: Int, operator: Int) {
        _addCartState.update { AddCartState.Loading }
        viewModelScope.launch {
            cartUseCase.addCart(token, colorProductId, quantity, operator).let { result ->
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

    fun getCart(token: String) {
        viewModelScope.launch {
            cartUseCase.getCart(token).let { result->
                when(result) {
                    is ResultWrapper.Failure -> {
                        _state.update { CartState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { CartState.Success(result.value as CartResponse) }
                        if (_isSelected.value.isEmpty()) {
                            val cartResponse = result.value as CartResponse
                            val cartItems = cartResponse.cartItems
                            cartItems.forEach { cartItem ->
                                _isSelected.update { List(cartItems.size) { true } }
                            }
                        }

                    }
                }
            }
        }
    }

    fun checkoutPreview(token: String, checkoutRequest: CheckoutRequest) {
        _stateCheckoutPreview.update { CheckoutPreviewState.Loading }
        viewModelScope.launch {
            cartUseCase.checkoutPreview(token, checkoutRequest).let { result ->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _stateCheckoutPreview.update { CheckoutPreviewState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _stateCheckoutPreview.update { CheckoutPreviewState.Success(result.value as CheckoutResponse) }
                    }
                }
            }
        }
    }

}

sealed class CartState {
    object Loading: CartState()
    data class Success(val data: CartResponse): CartState()
    data class Error(val message: String): CartState()
}

sealed class AddCartState {
    object Pending: AddCartState()
    object Loading: AddCartState()
    data class Success(val data: AddCartResponse): AddCartState()
    data class Error(val message: String): AddCartState()
}

sealed class CheckoutPreviewState {
    object Loading: CheckoutPreviewState()
    object Pending: CheckoutPreviewState()
    data class Success(val data: CheckoutResponse): CheckoutPreviewState()
    data class Error(val message: String): CheckoutPreviewState()
}

sealed class AddressState {
    object Loading: AddressState()
    object Pending: AddressState()
    data class Success(val data: AddressResponse): AddressState()
    data class Error(val message: String): AddressState()
}

sealed class SelectedAddressState {
    object Loading: SelectedAddressState()
    object Pending: SelectedAddressState()
    data class Success(val data: SelectedAddressResponse): SelectedAddressState()
    data class Error(val message: String): SelectedAddressState()
}

sealed class DeleteAddressState {
    object Loading: DeleteAddressState()
    object Pending: DeleteAddressState()
    data class Success(val data: DeleteAddressResponse): DeleteAddressState()
    data class Error(val message: String): DeleteAddressState()

}