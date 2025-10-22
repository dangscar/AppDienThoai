package com.nlhd.domain.usecase.shortvideo

data class ShortVideoUseCase(
    val getVideos: GetVideos,
    val getFollowUser: GetFollowUser,
    val follows: Follows,
    val likes: Likes,
    val favorites: Favorites,
    val getComments: GetComments,
    val addComment: AddComment,
    val getVideosSearch: GetVideosSearch,
    val getInfoProfile: GetInfoProfile,
    val getVideosByUser: GetVideoByUser,
    val increaseViews: IncreaseViews,
    val addVideo: AddVideo,
    val getVideosLiked: GetLikedVideos,
    val getVideosFavorite: GetFavoriteVideos,
    val getMyVideos: GetMyVideos,
    val deleteVideo: DeleteVideo,
    val updateCaption: UpdateCaption,
    val getFollowingVideos: GetFollowingVideos
)
