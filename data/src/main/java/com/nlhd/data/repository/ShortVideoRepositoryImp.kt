package com.nlhd.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.Message.MessageResponseDto
import com.nlhd.data.model.shortVideo.Comments.AddComment.AddCommentRequestDto
import com.nlhd.data.model.shortVideo.ProfileShortVideo.Info.InfoProfileResponseDto
import com.nlhd.data.model.shortVideo.UpdateCaptionVideo.UpdateCaptionRequest
import com.nlhd.data.remote.GetCommentsPagingSource
import com.nlhd.data.remote.GetFavoriteVideosPagingSource
import com.nlhd.data.remote.GetLikedVideosPagingSource
import com.nlhd.data.remote.GetMyVideoPagingSource
import com.nlhd.data.remote.GetVideoByUserPagingSource
import com.nlhd.data.remote.GetVideosPagingSource
import com.nlhd.data.remote.GetVideosSearchPagingSource
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse
import com.nlhd.domain.entity.shortVideo.UploadVideo.UploadVideo
import com.nlhd.domain.repository.ShortVideoRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.streams.asInput
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

    override fun getVideosSearch(
        token: String,
        search: String
    ): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                GetVideosSearchPagingSource(
                    ktor = ktor,
                    token = token,
                    search = search
                )
            }

        ).flow
    }

    override suspend fun getInfoProfile(
        token: String,
        userId: Int
    ): ResultWrapper<InfoProfileResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/video/profile/${userId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<InfoProfileResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override fun getVideosByUser(
        token: String,
        userId: Int
    ): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                GetVideoByUserPagingSource(
                    ktor = ktor,
                    token = token,
                    userId = userId
                )
            }
        ).flow
    }

    override suspend fun increaseViews(
        token: String,
        videoId: Int
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/video/view/${videoId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    private fun ContentResolver.displayName(uri: Uri): String {
        query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
            if (c.moveToFirst()) return c.getString(0)
        }
        return uri.lastPathSegment ?: "upload"
    }
    private fun ContentResolver.mimeType(uri: Uri): String =
        getType(uri) ?: "application/octet-stream"
    private fun ContentResolver.length(uri: Uri): Long? =
        openAssetFileDescriptor(uri, "r")?.length

    override suspend fun addVideo(
        token: String,
        uploadVideo: UploadVideo
    ): ResultWrapper<MessageResponse> {
        return try {
            val cr = uploadVideo.context.contentResolver

            val multipart = MultiPartFormDataContent(
                formData {
                    // IMAGE (optional)
                    uploadVideo.image?.let { img ->
                        appendInput(
                            key = "image",
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, cr.mimeType(img)) // ví dụ: image/jpeg
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "form-data; name=\"image\"; filename=\"${cr.displayName(img)}\""
                                )
                            },
                            size = cr.length(img) // có thể để null nếu không biết trước
                        ) { cr.openInputStream(img)!!.asInput() }
                    }

                    // VIDEO (required)
                    val vid = uploadVideo.video
                    appendInput(
                        key = "video",
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, cr.mimeType(vid)) // ví dụ: video/mp4
                            append(
                                HttpHeaders.ContentDisposition,
                                "form-data; name=\"video\"; filename=\"${cr.displayName(vid)}\""
                            )
                        },
                        size = cr.length(vid)
                    ) { cr.openInputStream(vid)!!.asInput() }

                    // TEXT fields
                    append("caption", uploadVideo.caption.orEmpty())
                }
            )

            val dto = ktor.post("${Utils.BASE_URL}/api/video") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(multipart)
            }.body<MessageResponseDto>()

            ResultWrapper.Success(dto.toDomain(dto))
        } catch (e: Exception) {
            ResultWrapper.Failure(e)

        }
    }

    override fun getVideosLiked(token: String): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 5),
            pagingSourceFactory = {
                GetLikedVideosPagingSource(
                    ktor = ktor,
                    token = token
                )
            }

        ).flow
    }

    override fun getVideosFavorite(token: String): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 5),
            pagingSourceFactory = {
                GetFavoriteVideosPagingSource(
                    ktor = ktor,
                    token = token
                )
            }

        ).flow
    }

    override fun getMyVideos(
        token: String,
        search: String
    ): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 5),
            pagingSourceFactory = {
                GetMyVideoPagingSource(
                    ktor = ktor,
                    token = token,
                    search = search
                )
            }

        ).flow
    }

    override suspend fun deleteVideo(
        token: String,
        videoId: Int
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.delete(Utils.BASE_URL+"/api/video/${videoId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun updateCaptionVideo(
        token: String,
        videoId: Int,
        caption: String?
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.patch(Utils.BASE_URL+"/api/video/${videoId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateCaptionRequest(
                        caption = caption
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