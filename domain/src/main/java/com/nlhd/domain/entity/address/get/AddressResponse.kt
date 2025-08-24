package com.nlhd.domain.entity.address.get

data class AddressResponse(
    val customerInfomations: List<CustomerInfomation>,
    val message: String
)