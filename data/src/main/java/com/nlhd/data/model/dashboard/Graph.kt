package com.nlhd.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class Graph(
    val month: Int,
    val revenue: Int,
    val year: Int
)