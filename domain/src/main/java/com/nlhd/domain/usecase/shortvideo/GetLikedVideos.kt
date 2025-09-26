package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetLikedVideos(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String) = repository.getVideosLiked(token)
}