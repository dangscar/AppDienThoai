package com.nlhd.domain.entity.UpdateVersionProduct


data class UpdateVersionProductRequest(
    val ram: String,
    val storage: String,
    val versionProductId: String
)