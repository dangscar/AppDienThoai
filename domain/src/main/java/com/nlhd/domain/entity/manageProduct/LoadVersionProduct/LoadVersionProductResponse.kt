package com.nlhd.domain.entity.manageProduct.LoadVersionProduct

data class LoadVersionProductResponse(
    val message: String,
    val versions: List<Version>
)