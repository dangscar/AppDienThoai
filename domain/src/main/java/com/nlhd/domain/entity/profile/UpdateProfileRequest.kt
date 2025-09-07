package com.nlhd.domain.entity.profile

data class UpdateProfileRequest(
    val address: String,
    val name: String,
    val phone: String
)