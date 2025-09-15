package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetInfoProfile(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, videoId: Int) = repository.getInfoProfile(token, videoId)
}