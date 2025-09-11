package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetComments(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String, videoId: String) = repository.getComments(token, videoId)
}