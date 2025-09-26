package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class DeleteVideo(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, videoId: Int) = repository.deleteVideo(token, videoId)
}