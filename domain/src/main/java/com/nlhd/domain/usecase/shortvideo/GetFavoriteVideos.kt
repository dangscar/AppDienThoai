package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.repository.ShortVideoRepository

class GetFavoriteVideos(
    private val repository: ShortVideoRepository
) {
    operator fun invoke(token: String) = repository.getVideosFavorite(token)
}