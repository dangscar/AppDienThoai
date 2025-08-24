package com.nlhd.domain.entity.manageProduct

import android.content.Context
import android.net.Uri

data class UploadProduct(
    val context: Context,
    val image: Uri,
    val name: String,
    val price: String,
    val status: String,
    val color: String,
    val value: String = "#000000",
    val category_id: String,
    val screenSize: String,
    val cpu: String,
    val ram: String,
    val storage: String,
    val camera: String,
    val battery: String,
    val os: String
)
