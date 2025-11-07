package com.nlhd.data.mapper

import com.nlhd.data.model.shortVideo.LikeShortVideo.FavoriteResponseDto
import com.nlhd.data.model.shortVideo.LikeShortVideo.LikeResponseDto
import com.nlhd.domain.entity.shortVideo.LikeShortVideo.FavoriteResponse
import com.nlhd.domain.entity.shortVideo.LikeShortVideo.LikeResponse

fun LikeResponseDto.toDomain(likeResponseDto: LikeResponseDto): LikeResponse {
    return LikeResponse(
        likeCount = like_count.toShortString(),
        message =  message
    )
}

fun FavoriteResponseDto.toDomain(favoriteResponseDto: FavoriteResponseDto): FavoriteResponse {
    return FavoriteResponse(
        favoriteCount = favorite_count.toShortString(),
        message = message
    )
}