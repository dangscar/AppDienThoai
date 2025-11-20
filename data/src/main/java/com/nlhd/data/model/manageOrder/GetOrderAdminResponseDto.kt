package com.nlhd.data.model.manageOrder

import kotlinx.serialization.Serializable

@Serializable
data class GetOrderAdminResponseDto(
    val current_page: Int,
    val `data`: List<Data>,
)