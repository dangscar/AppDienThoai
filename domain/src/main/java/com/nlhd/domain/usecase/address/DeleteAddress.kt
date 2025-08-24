package com.nlhd.domain.usecase.address

import com.nlhd.domain.repository.AddressRepository

class DeleteAddress(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(token: String, id: Int) = repository.deleteAddress(token, id)
}