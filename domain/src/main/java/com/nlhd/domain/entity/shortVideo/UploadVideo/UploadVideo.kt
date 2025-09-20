package com.nlhd.domain.entity.shortVideo.UploadVideo

import android.content.Context
import android.net.Uri

data class UploadVideo(
    val context: Context,
    val video: Uri,
    val image: Uri? = null,
    val caption: String? = null,
    val productId: Int? = null,
)