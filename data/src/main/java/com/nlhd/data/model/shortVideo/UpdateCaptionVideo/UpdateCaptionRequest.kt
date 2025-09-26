package com.nlhd.data.model.shortVideo.UpdateCaptionVideo

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCaptionRequest(val caption: String? = null)