package com.nlhd.domain.entity.address.update

data class UpdateAddressRequest(
    val address: String,
    val description: String,
    val id: Int,
    val name: String,
    val phone: String
)