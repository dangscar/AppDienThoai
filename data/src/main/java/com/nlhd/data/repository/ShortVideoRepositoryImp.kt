package com.nlhd.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.Message.MessageResponseDto
import com.nlhd.data.model.shortVideo.Comments.AddComment.AddCommentRequestDto
import com.nlhd.data.remote.GetCommentsPagingSource
import com.nlhd.data.remote.GetVideosPagingSource
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.repository.ShortVideoRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow

class ShortVideoRepositoryImp(
    private val ktor: HttpClient
): ShortVideoRepository {
    override fun getVideos(token: String): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                GetVideosPagingSource(
                    ktor = ktor,
                    token = token
                )
            }

        ).flow
    }

    override suspend fun follows(
        token: String,
        userId: String
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/follows/${userId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun likes(
        token: String,
        videoId: String
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/likes/${videoId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun favorites(
        token: String,
        videoId: String
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/favorites/${videoId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override fun getComments(
        token: String,
        videoId: String
    ): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                GetCommentsPagingSource(
                    ktor = ktor,
                    token = token,
                    videoId = videoId
                )
            }
        ).flow
    }

    override suspend fun addComment(
        token: String,
        addCommentRequest: AddCommentRequest
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/comments") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    AddCommentRequestDto(
                        content = addCommentRequest.content,
                        video_id = addCommentRequest.videoId
                    )
                )
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }
}