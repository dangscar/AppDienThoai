package com.nlhd.domain.entity.manageProduct.UpdateColorProduct

import android.content.Context
import android.net.Uri

data class UpdateColorRequest(
    val context: Context,
    val image: Uri? = null,
    val color: String,
    val colorId: Int,
    val price: Int,
    val status: String,
    val value: String,
)