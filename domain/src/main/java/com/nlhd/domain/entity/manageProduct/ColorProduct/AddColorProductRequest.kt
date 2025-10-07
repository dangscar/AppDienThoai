package com.nlhd.domain.entity.manageProduct.ColorProduct

import android.content.Context
import android.net.Uri

data class AddColorProductRequest(
    val context: Context,
    val image: Uri,
    val price: String,
    val color: String,
    val value: String = "#000000",
    val status: String,
    val versionId: Int
)
