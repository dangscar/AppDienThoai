package com.nlhd.data.model.manageProduct.LoadVersionProduct

import kotlinx.serialization.Serializable

@Serializable
data class LoadVersionProductResponseDto(
    val message: String,
    val versions: List<Version>
)