package com.nlhd.data.model.shortVideo.UploadVideo

import android.net.Uri

data class UploadVideoDto(
    val video: Uri,
    val image: Uri? = null,
    val caption: String? = null,
    val product_id: Int? = null,
)