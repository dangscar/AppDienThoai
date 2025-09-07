package com.nlhd.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseDto(
    val `data`: List<Data>,

)