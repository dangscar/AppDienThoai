package com.nlhd.shortvideo

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoViewModel(

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