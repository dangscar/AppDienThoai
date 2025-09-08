package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetVideos(
    private val repository: ShortVideoRepository
) {
    operator fun invoke() = repository.getVideos()
}