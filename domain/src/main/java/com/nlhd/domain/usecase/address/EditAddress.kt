package com.nlhd.domain.usecase.address

import com.nlhd.domain.repository.AddressRepository

class EditAddress(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(token: String, id: Int) = repository.editAddress(token, id)
}