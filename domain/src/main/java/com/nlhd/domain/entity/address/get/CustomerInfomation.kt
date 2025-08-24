package com.nlhd.domain.entity.address.get

data class CustomerInfomation(
    val address: String,
    val description: String? = null,
    val fullName: String,
    val id: Int,
    val isSelected: Int,
    val phoneNumber: String,
    val userId: Int
)