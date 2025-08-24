package com.nlhd.domain.entity.address.edit

data class CustomerInformation(
    val address: String,
    val description: String,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String,
)