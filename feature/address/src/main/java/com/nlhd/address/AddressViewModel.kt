package com.nlhd.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.address.add.AddAddressRequest
import com.nlhd.domain.entity.address.add.AddAddressResponse
import com.nlhd.domain.entity.address.edit.EditAddressResponse
import com.nlhd.domain.entity.address.update.UpdateAddressRequest
import com.nlhd.domain.entity.address.update.UpdateAddressResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.address.AddressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel (
    private val addressUseCase: AddressUseCase
): ViewModel() {

    private var _state = MutableStateFlow<Address>(Address(
        name = "",
        phone = "",
        address = "",
        description = ""
    ))
    val state = _state.asStateFlow()

    private var _stateAdd = MutableStateFlow<AddressState>(AddressState.Pending)
    val stateAdd = _stateAdd.asStateFlow()

    private var _stateUpdate = MutableStateFlow<UpdateAddressState>(UpdateAddressState.Pending)
    val stateUpdate = _stateUpdate.asStateFlow()

    fun updateAddress(token: String, id: Int) = viewModelScope.launch {
        _stateUpdate.update { UpdateAddressState.Loading }
        val updateAddressRequest = UpdateAddressRequest(
            id = id,
            name = _state.value.name,
            phone = _state.value.phone,
            address = _state.value.address,
            description = _state.value.description ?: ""
        )
        addressUseCase.updateAddress(token, updateAddressRequest).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateUpdate.update { UpdateAddressState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateUpdate.update { UpdateAddressState.Success(result.value as UpdateAddressResponse) }
                }
            }
        }
    }

    fun editAddress(token: String, id: Int) = viewModelScope.launch {
        addressUseCase.editAddress(token, id).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    val message = result.exception.message.toString()
                    _state.update {
                        it.copy(
                            name = "Chưa xác định",
                            phone = "0000000000",
                            address = "Chưa xác định",
                            description = "Chưa xác định"
                        )
                    }
                }
                is ResultWrapper.Success<*> -> {
                    val data = (result.value as EditAddressResponse).customerInformation
                    _state.update {
                        it.copy(
                            name = data.fullName,
                            phone = data.phoneNumber,
                            address = data.address,
                            description = data.description
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun setPhone(phone: String) {
        _state.update { it.copy(phone = phone) }
    }

    fun setAddress(address: String) {
        _state.update { it.copy(address = address) }
    }

    fun setDescription(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun addAddress(token: String) = viewModelScope.launch {
        _stateAdd.update { AddressState.Loading }
        val addAddressRequest = AddAddressRequest(
            name = _state.value.name,
            phone = _state.value.phone,
            address = _state.value.address,
            description = _state.value.description
        )

        addressUseCase.addAddress(token, addAddressRequest).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateAdd.update { AddressState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateAdd.update { AddressState.Success(result.value as AddAddressResponse) }
                }
            }
        }
    }


}

data class Address(
    val name: String,
    val phone: String,
    val address: String,
    val description: String? = null
)

sealed class AddressState {
    object Pending : AddressState()
    object Loading : AddressState()
    data class Success(val data: AddAddressResponse) : AddressState()
    data class Error(val message: String) : AddressState()
}

sealed class UpdateAddressState {
    object Pending : UpdateAddressState()
    object Loading : UpdateAddressState()
    data class Success(val data: UpdateAddressResponse) : UpdateAddressState()
    data class Error(val message: String) : UpdateAddressState()

}