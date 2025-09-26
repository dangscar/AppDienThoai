package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class UpdateCaption(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, videoId: Int, caption: String?) = repository.updateCaptionVideo(token, videoId, caption)
}