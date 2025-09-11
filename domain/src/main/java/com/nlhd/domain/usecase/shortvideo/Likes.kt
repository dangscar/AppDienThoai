package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class Likes(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, videoId: String) = repository.likes(token, videoId)
}