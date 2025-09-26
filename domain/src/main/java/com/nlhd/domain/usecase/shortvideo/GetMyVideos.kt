package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetMyVideos(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String, search: String) = repository.getMyVideos(token, search)
}