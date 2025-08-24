package com.nlhd.domain.usecase.address

import com.nlhd.domain.entity.address.update.UpdateAddressRequest
import com.nlhd.domain.repository.AddressRepository

class UpdateAddress(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(token: String, updateAddressRequest: UpdateAddressRequest) = repository.updateAddress(token, updateAddressRequest)
}