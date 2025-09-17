package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetVideoByUser(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String, userId: Int) = repository.getVideosByUser(token, userId)
}