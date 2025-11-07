package com.nlhd.domain.repository

import androidx.paging.PagingData
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.entity.shortVideo.LikeShortVideo.FavoriteResponse
import com.nlhd.domain.entity.shortVideo.LikeShortVideo.LikeResponse
import com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse
import com.nlhd.domain.entity.shortVideo.UploadVideo.UploadVideo
import com.nlhd.domain.resultWrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ShortVideoRepository {
    fun getVideos(token: String): Flow<PagingData<Video>>
    suspend fun getFollowUser(token: String, userId: String): ResultWrapper<MessageResponse>
    suspend fun follows(token: String, userId: String): ResultWrapper<MessageResponse>
    suspend fun likes (token: String, videoId: String): ResultWrapper<LikeResponse>
    suspend fun favorites (token: String, videoId: String): ResultWrapper<FavoriteResponse>
    fun getComments(token: String, videoId: String): Flow<PagingData<Comment>>
    suspend fun addComment(token: String, addCommentRequest: AddCommentRequest): ResultWrapper<MessageResponse>
    fun getVideosSearch(token: String, search: String): Flow<PagingData<Video>>
    suspend fun getInfoProfile(token: String, userId: Int): ResultWrapper<InfoProfileResponse>
    fun getVideosByUser(token: String, userId: Int): Flow<PagingData<Video>>
    suspend fun increaseViews(token: String, videoId: Int): ResultWrapper<MessageResponse>
    suspend fun addVideo(token: String, uploadVideo: UploadVideo): ResultWrapper<MessageResponse>
    fun getVideosLiked(token: String): Flow<PagingData<Video>>
    fun getVideosFavorite(token: String): Flow<PagingData<Video>>
    fun getMyVideos(token: String, search: String): Flow<PagingData<Video>>
    suspend fun deleteVideo(token: String, videoId: Int): ResultWrapper<MessageResponse>
    suspend fun updateCaptionVideo(token: String, videoId: Int, caption: String?): ResultWrapper<MessageResponse>
    fun getFollowingVideos(token: String): Flow<PagingData<Video>>
}