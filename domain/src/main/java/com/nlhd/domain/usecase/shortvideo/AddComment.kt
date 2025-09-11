package com.nlhd.domain.usecase.shortvideo

import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.repository.ShortVideoRepository

class AddComment(
    private val repository: ShortVideoRepository
) {
    suspend fun invoke(token: String, addCommentRequest: AddCommentRequest) = repository.addComment(token, addCommentRequest)
}