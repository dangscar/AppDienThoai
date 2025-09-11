package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class Favorites(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, videoId: String) = repository.favorites(token, videoId)
}