package com.nlhd.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.nlhd.data.model.shortVideo.Comments.AddComment.AddCommentRequestDto
import com.nlhd.data.model.shortVideo.Comments.GetComments.CommentResponseDto
import com.nlhd.data.model.shortVideo.GetVideos.Data
import com.nlhd.data.model.shortVideo.GetVideos.User
import com.nlhd.data.model.shortVideo.GetVideos.VideoResponseDto
import com.nlhd.data.model.shortVideo.ProfileShortVideo.Info.InfoProfileResponseDto
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.CommentResponse
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.entity.shortVideo.GetVideos.VideoResponse
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun VideoResponseDto.toDomain(videoResponseDto: VideoResponseDto): VideoResponse {
    return VideoResponse(
        videos = videoResponseDto.data.map {
            it.toDomain(it)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Data.toDomain(data: Data): Video {
    return Video(
        canFollow = data.can_follow == 1,
        caption = data.caption,
        comments = data.comments_count.toShortString(),
        createdAt = data.created_at?.toDateOnly(),
        favorites = data.favorites_count.toShortString(),
        id = data.id,
        isFollowing = data.is_following == 1,
        likes = data.likes_count.toShortString(),
        productId = data.product_id,
        shares = data.shares.toShortString(),
        thumbnailUrl = data.thumbnail_url,
        user = data.user.toDomain(data.user),
        videoUrl = data.video_url,
        views = data.views.toShortString(),
        isLiked = data.is_liked == 1,
        isFavorited = data.is_favorited == 1,
        versionId = data.version_id,
        colorId = data.color_id
    )
}

fun User.toDomain(user: User): com.nlhd.domain.entity.shortVideo.GetVideos.User {
    return com.nlhd.domain.entity.shortVideo.GetVideos.User(
        avatarUrl = user.avatar_url.toString(),
        id = user.id,
        name = user.name
    )
}

fun Int.toShortString(): String {
    return when {
        this >= 1_000_000_000 -> String.format("%.1fB", this / 1_000_000_000.0).removeSuffix(".0")
        this >= 1_000_000     -> String.format("%.1fM", this / 1_000_000.0).removeSuffix(".0")
        this >= 100_000       -> String.format("%dK", this / 1_000) // 100K, 250K...
        this >= 10_000        -> String.format("%dK", this / 1_000) // 10K, 15K...
        this >= 1_000         -> String.format("%.1fK", this / 1_000.0).removeSuffix(".0")
        else                  -> this.toString()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDateOnly(): String {
    val zoned = ZonedDateTime.parse(this) // parse từ chuỗi ISO 8601
    return zoned.toLocalDate().toString() // trả yyyy-MM-dd
}


//Comments
@RequiresApi(Build.VERSION_CODES.O)
fun CommentResponseDto.toDomain(commentResponseDto: CommentResponseDto): CommentResponse {
    return CommentResponse(
        comments = commentResponseDto.data.map {
            it.toDomain(it)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun com.nlhd.data.model.shortVideo.Comments.GetComments.Data.toDomain(data: com.nlhd.data.model.shortVideo.Comments.GetComments.Data): Comment {
    return Comment(
        content = data.content,
        createdAt = data.created_at.toDateOnly(),
        id = data.id,
        user = data.user.toDomain(data.user),
        userId = data.user_id,
        videoId = data.video_id
    )
}

fun com.nlhd.data.model.shortVideo.Comments.GetComments.User.toDomain(user: com.nlhd.data.model.shortVideo.Comments.GetComments.User): com.nlhd.domain.entity.shortVideo.Comments.GetComments.User {
    return com.nlhd.domain.entity.shortVideo.Comments.GetComments.User(
        avatarUrl = user.avatar_url,
        id = user.id,
        name = user.name
    )
}

fun AddCommentRequestDto.toDomain(addCommentRequestDto: AddCommentRequestDto): AddCommentRequest {
    return AddCommentRequest(
        content = addCommentRequestDto.content,
        videoId = addCommentRequestDto.video_id
    )
}

fun InfoProfileResponseDto.toDomain(infoProfileResponseDto: InfoProfileResponseDto): com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse {
    return com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse(
        caption = infoProfileResponseDto.caption,
        createdAt = infoProfileResponseDto.created_at,
        favoritesCount = infoProfileResponseDto.favorites_count,
        id = infoProfileResponseDto.id,
        likesCount = infoProfileResponseDto.likes_count,
        productId = infoProfileResponseDto.product_id,
        shares = infoProfileResponseDto.shares,
        thumbnailUrl = infoProfileResponseDto.thumbnail_url,
        user = infoProfileResponseDto.user.toDomain(infoProfileResponseDto.user),
        userId = infoProfileResponseDto.user_id,
        videoUrl = infoProfileResponseDto.video_url,
        views = infoProfileResponseDto.views.toShortString()
    )
}

fun com.nlhd.data.model.shortVideo.ProfileShortVideo.Info.User.toDomain(user: com.nlhd.data.model.shortVideo.ProfileShortVideo.Info.User): com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.User {
    return com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.User(
        avatarUrl = user.avatar_url,
        id = user.id,
        name = user.name,
        favoritesCount = user.received_favorites_count.toShortString(),
        likesCount = user.received_likes_count.toShortString(),
        email = user.email,
        followersCount = user.followers_count.toShortString(),
        followingsCount = user.followings_count.toShortString()
    )

}