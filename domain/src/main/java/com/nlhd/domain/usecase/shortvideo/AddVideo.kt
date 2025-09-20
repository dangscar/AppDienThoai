package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.entity.shortVideo.UploadVideo.UploadVideo
import com.nlhd.domain.repository.ShortVideoRepository

class AddVideo(
    private val repository: ShortVideoRepository
) {
    suspend operator fun invoke(token: String, uploadVideo: UploadVideo) = repository.addVideo(token, uploadVideo)
}