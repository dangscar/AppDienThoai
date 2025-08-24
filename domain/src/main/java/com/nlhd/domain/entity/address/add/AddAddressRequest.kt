package com.nlhd.domain.entity.address.add

data class AddAddressRequest(
    val address: String,
    val description: String? = null,
    val name: String,
    val phone: String
)