package com.nlhd.domain.usecase.address

import com.nlhd.domain.repository.AddressRepository

class GetAddress(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(token: String) = repository.getAddress(token)
}