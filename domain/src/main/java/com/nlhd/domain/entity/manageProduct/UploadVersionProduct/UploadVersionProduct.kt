package com.nlhd.domain.entity.manageProduct.UploadVersionProduct

import android.content.Context
import android.net.Uri

data class UploadVersionProduct(
    val context: Context,
    val image: Uri,
    val price: String,
    val status: String,
    val color: String,
    val value: String = "#000000",
    val ram: String,
    val storage: String,
    val productId: Int
)
