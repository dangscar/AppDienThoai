package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ShortVideoRepository {
    fun getVideos(token: String): Flow<PagingData<Video>>
    suspend fun follows(token: String, userId: String): ResultWrapper<MessageResponse>
    suspend fun likes (token: String, videoId: String): ResultWrapper<MessageResponse>
    suspend fun favorites (token: String, videoId: String): ResultWrapper<MessageResponse>
    fun getComments(token: String, videoId: String): Flow<PagingData<Comment>>
    suspend fun addComment(token: String, addCommentRequest: AddCommentRequest): ResultWrapper<MessageResponse>
    fun getVideosSearch(token: String, search: String): Flow<PagingData<Video>>
    suspend fun getInfoProfile(token: String, userId: Int): ResultWrapper<InfoProfileResponse>
    fun getVideosByUser(token: String, userId: Int): Flow<PagingData<Video>>
    suspend fun increaseViews(token: String, videoId: Int): ResultWrapper<MessageResponse>
}