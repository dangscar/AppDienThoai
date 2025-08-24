package com.nlhd.domain.usecase.address

import com.nlhd.domain.entity.address.add.AddAddressRequest
import com.nlhd.domain.repository.AddressRepository

class AddAddress(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(token: String, addAddressRequest: AddAddressRequest) = repository.addAddress(token, addAddressRequest)
}