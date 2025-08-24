package com.nlhd.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.checkout.CheckoutOrderRequest
import com.nlhd.domain.entity.checkout.CheckoutOrderResponse
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.checkout.CheckoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val checkoutUseCase: CheckoutUseCase
): ViewModel() {

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.Pending)
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    private var _payments = MutableStateFlow<List<Triple<Int, Boolean, String>>>(emptyList())
    val payments = _payments.asStateFlow()

    init {
        _payments.update {
            mutableListOf<Triple<Int, Boolean, String>>(
                Triple(0, true, "cod"),
                Triple(1, false, "gpay")
            )
        }
    }

    fun changePayment(index: Int) {
        _payments.update {
            it.mapIndexed { i, triple ->
                Triple(i, i == index, triple.third)
            }
        }
    }

    fun setState(state: CheckoutState) {
        _state.update { state }
    }

    fun checkoutOrder(token: String, checkoutOrderRequest: CheckoutOrderRequest) {
        _state.update { CheckoutState.Loading }
        viewModelScope.launch {
            checkoutUseCase.checkoutOrder(token, checkoutOrderRequest).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { CheckoutState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { CheckoutState.Success(result.value as CheckoutOrderResponse) }
                    }
                }
            }
        }
    }

}

sealed class CheckoutState {
    object Pending: CheckoutState()
    object Loading: CheckoutState()
    data class Success(val checkoutOrderResponse: CheckoutOrderResponse): CheckoutState()
    data class Error(val message: String): CheckoutState()

}