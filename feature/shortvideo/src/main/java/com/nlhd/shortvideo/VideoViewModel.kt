package com.nlhd.shortvideo

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.AddComment.AddCommentRequest
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {
    private var _videoState = MutableStateFlow(VideoState.IDLE)
    val videoState: StateFlow<VideoState> = _videoState.asStateFlow()

    private var _displayText = MutableStateFlow<DisplayText>(DisplayText.Hide())
    val displayText: StateFlow<DisplayText> = _displayText.asStateFlow()

    private var _actionButton = MutableStateFlow(ActionButton())
    val actionButton: StateFlow<ActionButton> = _actionButton.asStateFlow()

    private var _currentPosition = MutableStateFlow(0f)
    val currentPosition = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0f)
    val duration = _duration.asStateFlow()

    private val _imageStatus = MutableStateFlow(ImageStatus())
    val imageStatus = _imageStatus.asStateFlow()

    private var _stateFollow = MutableStateFlow<ShortVideoState>(ShortVideoState.Idle)
    val stateFollow = _stateFollow.asStateFlow()

    private var _stateLike = MutableStateFlow<ShortVideoState>(ShortVideoState.Idle)
    val stateLike = _stateLike.asStateFlow()

    private var _stateFavorite = MutableStateFlow<ShortVideoState>(ShortVideoState.Idle)
    val stateFavorite = _stateFavorite.asStateFlow()

    private var _contentComment = MutableStateFlow("")
    val contentComment = _contentComment.asStateFlow()

    private val commentsFlows = mutableMapOf<String, Flow<PagingData<Comment>>>()

    private var _addCommentState = MutableStateFlow<ShortVideoState>(ShortVideoState.Idle)
    val addCommentState = _addCommentState.asStateFlow()

    fun setAddCommentState(state: ShortVideoState) { _addCommentState.update { state } }

    fun addComment(token: String, videoId: Int) = viewModelScope.launch {
        shortVideoUseCase.addComment.invoke(token, AddCommentRequest(_contentComment.value, videoId)).let { result ->
            when (result) {
                is ResultWrapper.Failure -> { _addCommentState.update { ShortVideoState.Error(result.exception.message.toString()) } }
                is ResultWrapper.Success<*> -> { _addCommentState.update { ShortVideoState.Success(result.value as MessageResponse) } }
            }
        }
    }
    fun commentsFlow(token: String, videoId: String): Flow<PagingData<Comment>> {
        return commentsFlows.getOrPut(videoId) {
            shortVideoUseCase.getComments(token, videoId)  // trả về Pager(...)
                .cachedIn(viewModelScope)
        }
    }

    fun setContentComment(content: String) { _contentComment.update { content }  }

    fun setFavorite(favorite: Color) = _actionButton.update { it.copy(favorite = favorite) }

    fun setLike(like : Color) = _actionButton.update { it.copy(like = like) }

    fun setFollow(follow: Follow) = _actionButton.update { it.copy(avatar = follow) }

    fun favorite(token: String, videoId: String) = viewModelScope.launch {
        shortVideoUseCase.favorites.invoke(token, videoId).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateFavorite.update { ShortVideoState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateFavorite.update { ShortVideoState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun like(token: String, videoId: String) = viewModelScope.launch {
        shortVideoUseCase.likes.invoke(token, videoId).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateLike.update { ShortVideoState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateLike.update { ShortVideoState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun follows(token: String, userId: String) = viewModelScope.launch {
        shortVideoUseCase.follows.invoke(token, userId).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateFollow.update { ShortVideoState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _stateFollow.update { ShortVideoState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun onImageStatus(list: List<String>, index: Int) {
        _imageStatus.update {
            it.copy(list = list, selectedIndex = index)
        }
    }

    fun handleDuration(exoPlayer: ExoPlayer) {
        if (exoPlayer.duration >= 0f) {
            _duration.update { exoPlayer.duration.toFloat() }
        }
    }

    suspend fun updatePosition(exoPlayer: ExoPlayer) {
        while (true) {
            _currentPosition.update { exoPlayer.currentPosition.toFloat() }
            delay(500L)
        }
    }

    fun onValueChange(value: Float) {
        _currentPosition.update { value }
        _actionButton.update {
            it.copy(change = ChangeSlider.CHANGE)
        }
    }

    fun onValueFinish() {
        _actionButton.update {
            it.copy(change = ChangeSlider.NOT_CHANGE)
        }
    }

    fun onEvent(event: VideoState) = _videoState.update { event }

    fun onDisplayText() {
        if (_displayText.value == DisplayText.SeeMore()) {
            _displayText.update { DisplayText.Hide() }
        } else {
            _displayText.update { DisplayText.SeeMore() }
        }
    }

    fun onActionButton(perform: Perform) {
        when(perform) {
            is Perform.Like -> {
                if (_actionButton.value.like == Color.White) {
                    _actionButton.update {
                        it.copy(like = Color.Red)
                    }
                } else {
                    _actionButton.update {
                        it.copy(like = Color.White)
                    }
                }
            }

            is Perform.Avatar -> {
                if (_actionButton.value.avatar == Follow.Follow) {
                    _actionButton.update {
                        it.copy(avatar = Follow.UnFollow)
                    }
                } else {
                    _actionButton.update {
                        it.copy(avatar = Follow.Follow)
                    }
                }
            }
            is Perform.Comment -> {
                if (_actionButton.value.comment == ShowHide.Show) {
                    _actionButton.update {
                        it.copy(comment = ShowHide.Hide)
                    }
                } else {
                    _actionButton.update {
                        it.copy(comment = ShowHide.Show)
                    }
                }

            }
            is Perform.Favorite -> {
                if (_actionButton.value.favorite == Color.White) {
                    _actionButton.update {
                        it.copy(favorite = Color.Yellow)
                    }
                } else {
                    _actionButton.update {
                        it.copy(favorite = Color.White)
                    }
                }
            }
            is Perform.Share -> {
                if (_actionButton.value.share == ShowHide.Show) {
                    _actionButton.update {
                        it.copy(share = ShowHide.Hide)
                    }
                } else {
                    _actionButton.update {
                        it.copy(share = ShowHide.Show)
                    }
                }
            }

            is Perform.Change -> {
                //Perform in function onValueChange
            }

            is Perform.General -> {
                if (_actionButton.value.general == ShowHide.Show) {
                    _actionButton.update {
                        it.copy(general = ShowHide.Hide)
                    }
                } else {
                    _actionButton.update {
                        it.copy(general = ShowHide.Show)
                    }
                }
            }
        }
    }

}

enum class VideoState {
    IDLE, PAUSE, PLAY
}

sealed class DisplayText {
    data class SeeMore(val title: String = "See more"): DisplayText()
    data class Hide(val title: String = "Hide"): DisplayText()
}

data class ActionButton(
    val avatar: Follow = Follow.UnFollow,
    val like: Color = Color.White,
    val comment: ShowHide = ShowHide.Hide,
    val favorite: Color = Color.White,
    val share: ShowHide = ShowHide.Hide,
    val change: ChangeSlider = ChangeSlider.NOT_CHANGE,
    val general: ShowHide = ShowHide.Hide,
)
sealed class Perform {
    data class Avatar(val avatar: ShowHide = ShowHide.Hide): Perform()
    data class Like(val color: Color = Color.White): Perform()
    data class Comment(val comment: ShowHide = ShowHide.Hide): Perform()
    data class Favorite(val color: Color = Color.White): Perform()
    data class Share(val share: ShowHide = ShowHide.Hide): Perform()
    data class Change(val change: ChangeSlider = ChangeSlider.NOT_CHANGE): Perform()
    data class General(val general: ShowHide = ShowHide.Hide): Perform()
}


enum class ShowHide {
    Show, Hide
}

enum class Follow {
    Follow, UnFollow
}

//Slider
enum class ChangeSlider {
    CHANGE, NOT_CHANGE
}

data class ImageStatus(
    val list: List<String> = emptyList(),
    val selectedIndex: Int = 0,
)

sealed class ShortVideoState {
    object Idle: ShortVideoState()
    object Loading: ShortVideoState()
    data class Success(val data: MessageResponse): ShortVideoState()
    data class Error(val message: String): ShortVideoState()
}