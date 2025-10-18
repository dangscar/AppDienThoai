package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetFollowingVideos(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String) = repository.getFollowingVideos(token)
}