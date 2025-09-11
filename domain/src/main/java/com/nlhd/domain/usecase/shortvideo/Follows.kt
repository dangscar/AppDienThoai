package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class Follows(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, userId: String) = repository.follows(token, userId)
}