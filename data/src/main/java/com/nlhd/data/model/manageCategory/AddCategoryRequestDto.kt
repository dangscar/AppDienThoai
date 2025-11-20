package com.nlhd.data.model.manageCategory

import kotlinx.serialization.Serializable

@Serializable
data class AddCategoryRequestDto(
    val name: String
)